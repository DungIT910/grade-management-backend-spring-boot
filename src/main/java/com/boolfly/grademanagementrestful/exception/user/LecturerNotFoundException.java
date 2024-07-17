package com.boolfly.grademanagementrestful.exception.user;

import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;

public class LecturerNotFoundException extends GradeManagementRuntimeException {
    public LecturerNotFoundException(String id) {
        super("Lecturer " + id + " not found");
    }
}
