package com.boolfly.GradeManagementRestful.exception.user;

import com.boolfly.GradeManagementRestful.exception.base.GradeManagementRuntimeException;

public class StudentNotFoundException extends GradeManagementRuntimeException {
    public StudentNotFoundException() {
        super("Student not found");
    }
}
