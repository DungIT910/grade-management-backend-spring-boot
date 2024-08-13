package com.boolfly.grademanagementrestful.service;

import com.boolfly.grademanagementrestful.domain.Maingrade;
import com.boolfly.grademanagementrestful.service.base.PrepareDocumentData;
import com.boolfly.grademanagementrestful.service.model.UpdateGradeCsvArguments;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.function.Consumer;

public interface CsvService {
    ByteArrayOutputStream getSampleGradeCSV(String courseId, PrepareDocumentData prepareDocumentData);

    ByteArrayOutputStream getAllGradesCSV(String courseId, PrepareDocumentData prepareDocumentData);

    List<Maingrade> updateGradesCSV(String courseId, MultipartFile request, Consumer<UpdateGradeCsvArguments> processUpdateGradeCsv);
}
