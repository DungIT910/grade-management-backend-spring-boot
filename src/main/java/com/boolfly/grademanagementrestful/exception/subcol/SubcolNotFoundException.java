package com.boolfly.grademanagementrestful.exception.subcol;

import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;

public class SubcolNotFoundException extends GradeManagementRuntimeException {
    public SubcolNotFoundException(String id) {
        super("Subcol " + id + " not found");
    }
}
