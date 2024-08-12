package com.boolfly.grademanagementrestful.service;

import com.boolfly.grademanagementrestful.service.base.PrepareDocumentData;

import java.io.ByteArrayOutputStream;

public interface PdfService {
    ByteArrayOutputStream getAllGradesPDF(String courseId, PrepareDocumentData prepareDocumentData);
}
