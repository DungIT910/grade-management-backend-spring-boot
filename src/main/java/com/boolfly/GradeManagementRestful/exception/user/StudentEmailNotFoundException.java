package com.boolfly.GradeManagementRestful.exception.user;

import com.boolfly.GradeManagementRestful.exception.base.GradeManagementRuntimeException;

public class StudentEmailNotFoundException extends GradeManagementRuntimeException {
    public StudentEmailNotFoundException(String email) {
        super("Student with email " + email + " not found");
    }
}
