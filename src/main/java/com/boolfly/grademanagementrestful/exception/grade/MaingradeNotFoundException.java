package com.boolfly.grademanagementrestful.exception.grade;

import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;

public class MaingradeNotFoundException extends GradeManagementRuntimeException {
    public MaingradeNotFoundException() {
        super("Maingrade not found");
    }
}
