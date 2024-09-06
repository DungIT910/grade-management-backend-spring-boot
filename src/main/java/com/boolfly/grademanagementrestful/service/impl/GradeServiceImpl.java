package com.boolfly.grademanagementrestful.service.impl;

import com.boolfly.grademanagementrestful.api.dto.general.BatchRequest;
import com.boolfly.grademanagementrestful.api.dto.grade.MaingradeUpdateRequest;
import com.boolfly.grademanagementrestful.api.dto.grade.SearchGradeRequest;
import com.boolfly.grademanagementrestful.api.dto.grade.SubgradeUpdateRequest;
import com.boolfly.grademanagementrestful.builder.base.SearchParamsBuilder;
import com.boolfly.grademanagementrestful.builder.grade.GradeSearchParamsBuilder;
import com.boolfly.grademanagementrestful.constant.GenericConstant;
import com.boolfly.grademanagementrestful.constant.maingrade.MaingradeConstant;
import com.boolfly.grademanagementrestful.constant.user.UserConstant;
import com.boolfly.grademanagementrestful.domain.*;
import com.boolfly.grademanagementrestful.domain.model.maingrade.MaingradeStatus;
import com.boolfly.grademanagementrestful.domain.model.subcol.SubcolStatus;
import com.boolfly.grademanagementrestful.exception.grade.LockedGradeException;
import com.boolfly.grademanagementrestful.exception.grade.MaingradeNotFoundException;
import com.boolfly.grademanagementrestful.exception.subcol.SubcolNotFoundException;
import com.boolfly.grademanagementrestful.exception.user.StudentNotFoundException;
import com.boolfly.grademanagementrestful.listener.event.MailEvent;
import com.boolfly.grademanagementrestful.repository.MaingradeRepository;
import com.boolfly.grademanagementrestful.repository.SubcolRepository;
import com.boolfly.grademanagementrestful.repository.SubgradeRepository;
import com.boolfly.grademanagementrestful.repository.UserRepository;
import com.boolfly.grademanagementrestful.service.GradeService;
import com.boolfly.grademanagementrestful.service.model.UpdateGradeCsvArguments;
import com.boolfly.grademanagementrestful.util.ParsingUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import io.hypersistence.tsid.TSID;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {
    private final UserRepository userRepository;
    private final SubgradeRepository subgradeRepository;
    private final SubcolRepository subcolRepository;
    private final MaingradeRepository maingradeRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Page<Maingrade> getGrades(int page, int size, SearchGradeRequest request) {
        SearchParamsBuilder paramsBuilder = GradeSearchParamsBuilder.from(page, size, request);
        Optional<BooleanExpression> predicate = paramsBuilder.getCommonCriteria();
        Pageable pageable = paramsBuilder.getPageable();
        return predicate.map(pre -> maingradeRepository.findAll(pre, pageable))
                .orElseGet(() -> maingradeRepository.findAll(pageable));
    }

    @Override
    public Maingrade updateMaingrade(MaingradeUpdateRequest request) {
        Long maingradeId = TSID.from(request.getMaingradeId()).toLong();
        return maingradeRepository.findByIdAndStatusNot(maingradeId, MaingradeStatus.INACTIVE)
                .map(maingrade ->
                        maingradeRepository.save(
                                setMaingradeFromRequest(
                                        maingrade,
                                        request.getMidtermGrade(),
                                        request.getFinalGrade(),
                                        request.isLocked()
                                )
                        )
                )
                .orElseThrow(MaingradeNotFoundException::new);
    }

    @Override
    public Subgrade updateSubgrade(SubgradeUpdateRequest request) {
        TSID subcolId = TSID.from(request.getSubcolId());
        Long studentLongId = TSID.from(request.getStudentId()).toLong();

        Subcol subcol = subcolRepository.findByIdAndStatus(subcolId.toLong(), SubcolStatus.ACTIVE)
                .orElseThrow(() -> new SubcolNotFoundException(subcolId.toString()));

        User student = userRepository.findByIdAndActiveTrue(studentLongId)
                .orElseThrow(StudentNotFoundException::new);

        Course course = subcol.getCourse();
        Long courseId = course.getId();

        if (maingradeRepository.notExistsByCourse_IdAndStudent_IdAndStatusNot(
                courseId,
                studentLongId,
                MaingradeStatus.INACTIVE)) {
            throw new MaingradeNotFoundException();
        }

        if (maingradeRepository.existsByCourse_IdAndStudent_IdAndStatus(
                courseId,
                studentLongId,
                MaingradeStatus.LOCKED)) {
            throw new LockedGradeException();
        }

        return addOrUpdateSubgrade(subcol, student, request.getGrade());
    }

    @Override
    @Transactional
    public List<Maingrade> updateMaingradeBatch(BatchRequest<MaingradeUpdateRequest> request) {
        List<MaingradeUpdateRequest> batch = Optional.ofNullable(request.getBatch())
                .orElseGet(List::of);
        List<Maingrade> mgList = new ArrayList<>();
        batch.forEach(item -> {
                    Long maingradeId = TSID.from(item.getMaingradeId()).toLong();
                    Maingrade mg = maingradeRepository.findByIdAndStatusNot(
                                    maingradeId,
                                    MaingradeStatus.INACTIVE
                            )
                            .orElseThrow(MaingradeNotFoundException::new);

                    mgList.add(
                            setMaingradeFromRequest(
                                    mg,
                                    item.getMidtermGrade(),
                                    item.getFinalGrade(),
                                    item.isLocked()
                            )
                    );
                }
        );
        return maingradeRepository.saveAll(mgList);
    }

    @Override
    public List<Subgrade> updateSubgradeBatch(BatchRequest<SubgradeUpdateRequest> request) {
        List<SubgradeUpdateRequest> batch = Optional.ofNullable(request.getBatch())
                .orElse(List.of());

        return batch.stream()
                .map(this::updateSubgrade)
                .toList();
    }

    protected Subgrade addOrUpdateSubgrade(Subcol subcol, User student, Double grade) {
        Long studentLongId = student.getId();
        Long subcolLongId = subcol.getId();

        Subgrade subgrade = subgradeRepository.findBySubcol_IdAndStudent_Id(subcolLongId, studentLongId)
                .orElseGet(() -> subgradeRepository.save(
                                Subgrade.builder()
                                        .id(TSID.fast().toLong())
                                        .subcol(subcol)
                                        .student(student)
                                        .build()
                        )
                );
        subgrade.setGrade(grade);

        return subgradeRepository.save(subgrade);
    }

    protected Maingrade setMaingradeFromRequest(Maingrade mg,
                                                Double midtermGrade,
                                                Double finalGrade,
                                                boolean locked) {
        if (MaingradeStatus.LOCKED.equals(mg.getStatus())) {
            throw new LockedGradeException();
        }

        mg.setMidtermGrade(midtermGrade);
        mg.setFinalGrade(finalGrade);

        if (locked) {
            String courseName = mg.getCourse().getName();
            String toEmail = mg.getStudent().getEmail();
            eventPublisher.publishEvent(
                    MailEvent
                            .builder()
                            .source(this)
                            .courseName(courseName)
                            .toEmail(toEmail)
                            .build()
            );
            mg.setStatus(MaingradeStatus.LOCKED);
        }
        return mg;
    }

    @Override
    public void processUpdateGradeCsv(UpdateGradeCsvArguments updateGradeCsvArguments) {
        Map<String, String> line = updateGradeCsvArguments.getLine();
        Long maingradeId = TSID.from(line.get(MaingradeConstant.MAINGRADE_ID)).toLong();
        Double midtermGrade = ParsingUtils.parseDouble(line.get(MaingradeConstant.MIDTERM_GRADE));
        Double finalGrade = ParsingUtils.parseDouble(line.get(MaingradeConstant.FINAL_GRADE));
        boolean locked = Boolean.parseBoolean(line.get(MaingradeConstant.LOCKED));

        Maingrade maingrade = maingradeRepository
                .findByIdAndStatusNot(maingradeId, MaingradeStatus.INACTIVE)
                .orElseThrow(MaingradeNotFoundException::new);

        setMaingradeFromRequest(maingrade, midtermGrade, finalGrade, locked);

        User student = maingrade.getStudent();
        List<Subcol> subcolList = updateGradeCsvArguments.getSubcolList();
        for (Subcol subcol : subcolList) {
            Double subgradeValue = ParsingUtils.parseDouble(line.get(subcol.getName()));
            addOrUpdateSubgrade(subcol, student, subgradeValue);
        }

        List<Maingrade> maingradeList = updateGradeCsvArguments.getMaingradeList();
        maingradeList.add(maingradeRepository.save(maingrade));
    }

    @Override
    public List<String> getGenericFieldNames(String id) {
        List<String> fieldNames = new ArrayList<>(List.of(
                MaingradeConstant.MIDTERM_GRADE,
                MaingradeConstant.FINAL_GRADE
        ));
        subcolRepository.findAllByCourse_IdAndStatus(TSID.from(id).toLong(), SubcolStatus.ACTIVE)
                .stream()
                .map(Subcol::getName)
                .forEach(fieldNames::add);
        return fieldNames;
    }

    @Override
    public List<Map<String, String>> getData(String id) {
        Long courseId = TSID.from(id).toLong();
        List<Map<String, String>> gradeRecords = new ArrayList<>();
        AtomicInteger index = new AtomicInteger(1);

        maingradeRepository.findAllByCourse_IdAndStatusNot(courseId, MaingradeStatus.INACTIVE)
                .forEach(mg -> {
                    User student = mg.getStudent();
                    Map<String, String> gradeRecord = new HashMap<>();
                    gradeRecord.put(GenericConstant.INDEX, String.valueOf(index.get()));
                    gradeRecord.put(UserConstant.STUDENT_ID, TSID.from(student.getId()).toString());
                    gradeRecord.put(UserConstant.LAST_NAME, student.getLastName());
                    gradeRecord.put(UserConstant.FIRST_NAME, student.getFirstName());
                    gradeRecord.put(
                            MaingradeConstant.MIDTERM_GRADE,
                            Optional.ofNullable(mg.getMidtermGrade())
                                    .map(Objects::toString)
                                    .orElse("")
                    );
                    gradeRecord.put(
                            MaingradeConstant.FINAL_GRADE,
                            Optional.ofNullable(mg.getFinalGrade())
                                    .map(Objects::toString)
                                    .orElse("")
                    );

                    subcolRepository.findAllByCourse_IdAndStatus(courseId, SubcolStatus.ACTIVE)
                            .forEach(subcol -> {
                                String subcolName = subcol.getName();
                                String grade = subgradeRepository.findBySubcol_IdAndStudent_Id(
                                                subcol.getId(),
                                                student.getId()
                                        )
                                        .map(Subgrade::getGrade)
                                        .map(Objects::toString)
                                        .orElse("");
                                gradeRecord.put(subcolName, grade);
                            });

                    gradeRecords.add(gradeRecord);
                    index.getAndIncrement();
                });

        return gradeRecords;
    }
}