package com.boolfly.grademanagementrestful.service.impl;

import com.boolfly.grademanagementrestful.api.dto.general.BatchRequest;
import com.boolfly.grademanagementrestful.api.dto.grade.MaingradeUpdateRequest;
import com.boolfly.grademanagementrestful.api.dto.grade.SearchGradeRequest;
import com.boolfly.grademanagementrestful.api.dto.grade.SubgradeUpdateRequest;
import com.boolfly.grademanagementrestful.builder.base.SearchParamsBuilder;
import com.boolfly.grademanagementrestful.builder.grade.GradeSearchParamsBuilder;
import com.boolfly.grademanagementrestful.domain.Maingrade;
import com.boolfly.grademanagementrestful.domain.Subcol;
import com.boolfly.grademanagementrestful.domain.Subgrade;
import com.boolfly.grademanagementrestful.domain.User;
import com.boolfly.grademanagementrestful.domain.model.maingrade.MaingradeStatus;
import com.boolfly.grademanagementrestful.domain.model.subcol.SubcolStatus;
import com.boolfly.grademanagementrestful.domain.model.subgrade.SubgradeStatus;
import com.boolfly.grademanagementrestful.exception.grade.MaingradeNotFoundException;
import com.boolfly.grademanagementrestful.exception.grade.UpdateErrorMaingradeBatchException;
import com.boolfly.grademanagementrestful.exception.grade.UpdateErrorSubgradeBatchException;
import com.boolfly.grademanagementrestful.exception.subcol.SubcolNotFoundException;
import com.boolfly.grademanagementrestful.exception.user.StudentNotFoundException;
import com.boolfly.grademanagementrestful.repository.MaingradeRepository;
import com.boolfly.grademanagementrestful.repository.SubcolRepository;
import com.boolfly.grademanagementrestful.repository.SubgradeRepository;
import com.boolfly.grademanagementrestful.repository.UserRepository;
import com.boolfly.grademanagementrestful.service.GradeService;
import com.querydsl.core.types.dsl.BooleanExpression;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {
    private final MaingradeRepository maingradeRepository;
    private final SubgradeRepository subgradeRepository;
    private final SubcolRepository subcolRepository;
    private final UserRepository userRepository;

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
        return maingradeRepository.findByIdAndStatus(maingradeId, MaingradeStatus.ACTIVE)
                .map(maingrade -> {
                    Optional.ofNullable(request.getMidtermGrade())
                            .ifPresent(maingrade::setMidtermGrade);
                    Optional.ofNullable(request.getFinalGrade())
                            .ifPresent(maingrade::setFinalGrade);
                    return maingradeRepository.save(maingrade);
                }).orElseThrow(MaingradeNotFoundException::new);
    }

    @Override
    public Subgrade updateSubgrade(SubgradeUpdateRequest request) {
        TSID subcolId = TSID.from(request.getSubcolId());
        TSID studentId = TSID.from(request.getStudentId());
        return subcolRepository.findByIdAndStatus(subcolId.toLong(), SubcolStatus.ACTIVE)
                .map(subcol -> userRepository.findByIdAndActiveTrue(studentId.toLong())
                        .map(student -> Optional.ofNullable(addOrUpdateSubgrade(subcol, student, request.getGrade()))
                                .orElseThrow(MaingradeNotFoundException::new))
                        .orElseThrow(StudentNotFoundException::new))
                .orElseThrow(() -> new SubcolNotFoundException(subcolId.toString()));
    }

    @Override
    public List<Maingrade> updateMaingradeBatch(BatchRequest<MaingradeUpdateRequest> request) {
        List<MaingradeUpdateRequest> errorList = new ArrayList<>();
        return Optional.ofNullable(request.getBatch())
                .map(batch -> {
                    List<Maingrade> mgList = new ArrayList<>();
                    batch.forEach(mq -> maingradeRepository.findByIdAndStatus(TSID.from(mq.getMaingradeId()).toLong(), MaingradeStatus.ACTIVE)
                            .ifPresentOrElse(mg -> {
                                if (mq.isUpdatedMidtermGrade()) {
                                    mg.setMidtermGrade(mq.getMidtermGrade());
                                }
                                if (mq.isUpdateFinalGrade()) {
                                    mg.setFinalGrade(mq.getFinalGrade());
                                }
                                mgList.add(mg);
                            }, () -> errorList.add(mq))
                    );
                    List<Maingrade> maingradeList = maingradeRepository.saveAll(mgList);
                    if (!errorList.isEmpty()) {
                        throw new UpdateErrorMaingradeBatchException(errorList.toString());
                    }
                    return maingradeList;
                }).orElseGet(List::of);
    }

    @Override
    public List<Subgrade> updateSubgradeBatch(BatchRequest<SubgradeUpdateRequest> request) {
        List<SubgradeUpdateRequest> errorList = new ArrayList<>();
        return Optional.ofNullable(request.getBatch())
                .map(batch -> {
                    List<Subgrade> subgradeList = new ArrayList<>();
                    batch.forEach(sub -> {
                        TSID subcolId = TSID.from(sub.getSubcolId());
                        TSID studentId = TSID.from(sub.getStudentId());
                        subcolRepository.findByIdAndStatus(subcolId.toLong(), SubcolStatus.ACTIVE)
                                .flatMap(subcol -> userRepository.findByIdAndActiveTrue(studentId.toLong())
                                        .flatMap(student -> Optional.ofNullable(addOrUpdateSubgrade(subcol, student, sub.getGrade()))))
                                .ifPresentOrElse(subgradeList::add, () -> errorList.add(sub));
                    });
                    if (!errorList.isEmpty()) {
                        throw new UpdateErrorSubgradeBatchException(errorList.toString());
                    }
                    return subgradeList;
                }).orElseGet(List::of);
    }


    private Subgrade addOrUpdateSubgrade(Subcol subcol, User student, Double grade) {
        Long studentId = student.getId();
        return subgradeRepository.findBySubcol_IdAndStudent_Id(subcol.getId(), studentId)
                .map(subgrade -> {
                    subgrade.setGrade(grade);
                    return subgradeRepository.save(subgrade);
                }).orElseGet(() -> maingradeRepository.findByCourse_IdAndStudent_IdAndStatus(
                                subcol.getCourse().getId(), studentId, MaingradeStatus.ACTIVE)
                        .map(maingrade -> subgradeRepository.save(
                                Subgrade.builder()
                                        .id(TSID.fast().toLong())
                                        .subcol(subcol)
                                        .student(student)
                                        .grade(grade)
                                        .status(SubgradeStatus.ACTIVE)
                                        .build()))
                        .orElse(null));
    }
}
