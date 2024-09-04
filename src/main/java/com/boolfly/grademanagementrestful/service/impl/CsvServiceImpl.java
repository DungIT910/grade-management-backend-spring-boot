package com.boolfly.grademanagementrestful.service.impl;

import com.boolfly.grademanagementrestful.constant.GenericConstant;
import com.boolfly.grademanagementrestful.constant.maingrade.MaingradeConstant;
import com.boolfly.grademanagementrestful.constant.user.UserConstant;
import com.boolfly.grademanagementrestful.domain.Maingrade;
import com.boolfly.grademanagementrestful.domain.Subcol;
import com.boolfly.grademanagementrestful.domain.model.course.CourseStatus;
import com.boolfly.grademanagementrestful.domain.model.maingrade.MaingradeStatus;
import com.boolfly.grademanagementrestful.domain.model.subcol.SubcolStatus;
import com.boolfly.grademanagementrestful.exception.course.CourseNotFoundException;
import com.boolfly.grademanagementrestful.exception.grade.MaingradeNotFoundException;
import com.boolfly.grademanagementrestful.repository.CourseRepository;
import com.boolfly.grademanagementrestful.repository.MaingradeRepository;
import com.boolfly.grademanagementrestful.repository.SubcolRepository;
import com.boolfly.grademanagementrestful.service.CsvService;
import com.boolfly.grademanagementrestful.service.base.AbstractCsvService;
import com.boolfly.grademanagementrestful.service.base.PrepareDocumentData;
import com.boolfly.grademanagementrestful.service.model.UpdateGradeCsvArguments;
import io.hypersistence.tsid.TSID;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class CsvServiceImpl extends AbstractCsvService implements CsvService {
    private final CourseRepository courseRepository;
    private final MaingradeRepository maingradeRepository;
    private final SubcolRepository subcolRepository;

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_LECTURER')")
    public ByteArrayOutputStream getSampleGradeCSV(String courseId, PrepareDocumentData prepareDocumentData) {
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

        fieldNames.addAll(prepareDocumentData.getGenericFieldNames(courseId));

        return writeFile(fieldNames, List.of());
    }

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_LECTURER')")
    public ByteArrayOutputStream getAllGradesCSV(String courseId, PrepareDocumentData prepareDocumentData) {
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
        fieldNames.addAll(prepareDocumentData.getGenericFieldNames(courseId));

        return writeFile(fieldNames, prepareDocumentData.getData(courseId));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_LECTURER')")
    public List<Maingrade> updateGradesCSV(String courseId,
                                           MultipartFile request,
                                           Consumer<UpdateGradeCsvArguments> processUpdateGradeCsv) {
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
}
