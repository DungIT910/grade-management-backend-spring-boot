package com.boolfly.grademanagementrestful.exception.subcol;

import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;

public class SubcolLimitException extends GradeManagementRuntimeException {
    public SubcolLimitException() {
        super("Subcol has reached the limit of 3 columns");
    }
}
