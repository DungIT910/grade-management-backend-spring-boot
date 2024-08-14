package com.boolfly.grademanagementrestful.service.impl;

import com.boolfly.grademanagementrestful.constant.GenericConstant;
import com.boolfly.grademanagementrestful.constant.user.UserConstant;
import com.boolfly.grademanagementrestful.domain.model.course.CourseStatus;
import com.boolfly.grademanagementrestful.domain.model.maingrade.MaingradeStatus;
import com.boolfly.grademanagementrestful.exception.course.CourseNotFoundException;
import com.boolfly.grademanagementrestful.exception.grade.MaingradeNotFoundException;
import com.boolfly.grademanagementrestful.repository.CourseRepository;
import com.boolfly.grademanagementrestful.repository.MaingradeRepository;
import com.boolfly.grademanagementrestful.service.PdfService;
import com.boolfly.grademanagementrestful.service.base.AbstractPdfService;
import com.boolfly.grademanagementrestful.service.base.PrepareDocumentData;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfServiceImpl extends AbstractPdfService implements PdfService {
    private final CourseRepository courseRepository;
    private final MaingradeRepository maingradeRepository;

    @Override
    public ByteArrayOutputStream getAllGradesPDF(String courseId, PrepareDocumentData prepareDocumentData) {
        Long courseLongId = TSID.from(courseId).toLong();

        String courseName = courseRepository
                .findNameByIdAndStatus(courseLongId, CourseStatus.ACTIVE)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        if (maingradeRepository.notExistsByCourse_IdAndStatusNot(courseLongId, MaingradeStatus.INACTIVE)) {
            throw new MaingradeNotFoundException();
        }

        Paragraph title = new Paragraph("GRADE REPORT FOR COURSE %s".formatted(courseName.toUpperCase()))
                .setBold()
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER);

        List<String> fieldNames = new ArrayList<>(List.of(
                GenericConstant.INDEX,
                UserConstant.STUDENT_ID,
                UserConstant.LAST_NAME,
                UserConstant.FIRST_NAME
        ));
        fieldNames.addAll(prepareDocumentData.getGenericFieldNames(courseId));

        return writeFile(title, fieldNames, prepareDocumentData.getData(courseId));
    }
}
