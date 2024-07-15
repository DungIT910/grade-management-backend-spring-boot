package com.boolfly.grademanagementrestful.exception.user;

import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;

public class StudentEmailNotFoundException extends GradeManagementRuntimeException {
    public StudentEmailNotFoundException(String email) {
        super("Student with email " + email + " not found");
    }
}
