package com.boolfly.grademanagementrestful.exception.user;

import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;

public class StudentNotFoundException extends GradeManagementRuntimeException {
    public StudentNotFoundException() {
        super("Student not found");
    }
}
