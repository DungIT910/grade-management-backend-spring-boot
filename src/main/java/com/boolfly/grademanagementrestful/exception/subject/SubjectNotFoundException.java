package com.boolfly.grademanagementrestful.exception.subject;

import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;

public class SubjectNotFoundException extends GradeManagementRuntimeException {
    public SubjectNotFoundException(String id) {
        super("Subject " + id + " not found");
    }
}
