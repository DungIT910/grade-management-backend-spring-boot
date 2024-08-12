package com.boolfly.grademanagementrestful.service.impl;

import com.boolfly.grademanagementrestful.constant.GenericConstant;
import com.boolfly.grademanagementrestful.constant.maingrade.MaingradeConstant;
import com.boolfly.grademanagementrestful.constant.user.UserConstant;
import com.boolfly.grademanagementrestful.domain.Maingrade;
import com.boolfly.grademanagementrestful.domain.Subcol;
import com.boolfly.grademanagementrestful.domain.Subgrade;
import com.boolfly.grademanagementrestful.domain.User;
import com.boolfly.grademanagementrestful.domain.model.course.CourseStatus;
import com.boolfly.grademanagementrestful.domain.model.maingrade.MaingradeStatus;
import com.boolfly.grademanagementrestful.domain.model.subcol.SubcolStatus;
import com.boolfly.grademanagementrestful.exception.course.CourseNotFoundException;
import com.boolfly.grademanagementrestful.exception.grade.MaingradeNotFoundException;
import com.boolfly.grademanagementrestful.repository.CourseRepository;
import com.boolfly.grademanagementrestful.repository.MaingradeRepository;
import com.boolfly.grademanagementrestful.repository.SubcolRepository;
import com.boolfly.grademanagementrestful.repository.SubgradeRepository;
import com.boolfly.grademanagementrestful.service.CsvService;
import com.boolfly.grademanagementrestful.service.base.AbstractCsvService;
import com.boolfly.grademanagementrestful.service.model.UpdateGradeCsvArguments;
import io.hypersistence.tsid.TSID;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class CsvServiceImpl extends AbstractCsvService implements CsvService {
    private final CourseRepository courseRepository;
    private final MaingradeRepository maingradeRepository;
    private final SubcolRepository subcolRepository;
    private final SubgradeRepository subgradeRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public ByteArrayOutputStream getSampleGradeCSV(String courseId) {
        Long courseLongId = TSID.from(courseId).toLong();

        if (courseRepository.notExistsByIdAndStatusNot(courseLongId, CourseStatus.INACTIVE)) {
            throw new CourseNotFoundException(courseId);
        }

        if (maingradeRepository.notExistsByCourse_IdAndStatusNot(courseLongId, MaingradeStatus.INACTIVE)) {
            throw new MaingradeNotFoundException();
        }

        List<String> fieldNames = new ArrayList<>(List.of(
                MaingradeConstant.MAINGRADE_ID,
                MaingradeConstant.LOCKED
        ));

        fieldNames.addAll(getGenericFieldNames(courseLongId));

        return writeFile(fieldNames, List.of());
    }

    @Override
    public ByteArrayOutputStream getAllGradesCSV(String courseId) {
        Long courseLongId = TSID.from(courseId).toLong();

        if (courseRepository.notExistsByIdAndStatusNot(courseLongId, CourseStatus.INACTIVE)) {
            throw new CourseNotFoundException(courseId);
        }

        if (maingradeRepository.notExistsByCourse_IdAndStatusNot(courseLongId, MaingradeStatus.INACTIVE)) {
            throw new MaingradeNotFoundException();
        }

        List<String> fieldNames = new ArrayList<>(List.of(
                GenericConstant.INDEX,
                UserConstant.STUDENT_ID,
                UserConstant.LAST_NAME,
                UserConstant.FIRST_NAME
        ));
        fieldNames.addAll(getGenericFieldNames(courseLongId));

        return writeFile(fieldNames, getData(courseLongId));
    }

    @Override
    @Transactional
    public List<Maingrade> updateGradesCSV(String courseId, MultipartFile request, Consumer<UpdateGradeCsvArguments> processUpdateGradeCsv) {
        Long courseLongId = TSID.from(courseId).toLong();
        List<Maingrade> maingradeList = new ArrayList<>();

        if (courseRepository.notExistsByIdAndStatusNot(courseLongId, CourseStatus.INACTIVE)) {
            throw new CourseNotFoundException(courseId);
        }

        List<Subcol> subcolList = subcolRepository.findAllByCourse_IdAndStatus(courseLongId, SubcolStatus.ACTIVE);

        readFile(request, line -> processUpdateGradeCsv.accept(UpdateGradeCsvArguments.builder()
                .line(line)
                .maingradeList(maingradeList)
                .subcolList(subcolList)
                .build()));

        return maingradeList;
    }

    public List<String> getGenericFieldNames(Long courseId) {
        List<String> fieldNames = new ArrayList<>(List.of(
                MaingradeConstant.MIDTERM_GRADE,
                MaingradeConstant.FINAL_GRADE
        ));
        subcolRepository.findAllByCourse_IdAndStatus(courseId, SubcolStatus.ACTIVE)
                .stream()
                .map(Subcol::getName)
                .forEach(fieldNames::add);
        return fieldNames;
    }


    public List<Map<String, String>> getData(Long courseLongId) {
        List<Map<String, String>> gradeRecords = new ArrayList<>();
        AtomicInteger index = new AtomicInteger(1);

        maingradeRepository.findAllByCourse_IdAndStatusNot(courseLongId, MaingradeStatus.INACTIVE)
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

                    subcolRepository.findAllByCourse_IdAndStatus(courseLongId, SubcolStatus.ACTIVE)
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
