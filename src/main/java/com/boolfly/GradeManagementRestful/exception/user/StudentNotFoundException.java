package com.boolfly.GradeManagementRestful.exception.user;

import com.boolfly.GradeManagementRestful.exception.base.GradeManagementRuntimeException;

public class StudentNotFoundException extends GradeManagementRuntimeException {
    public StudentNotFoundException(String email) {
        super("Student with email " + email + " not found");
    }
}
