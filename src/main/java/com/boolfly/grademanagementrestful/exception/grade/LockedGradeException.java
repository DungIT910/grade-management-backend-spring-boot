package com.boolfly.grademanagementrestful.exception.grade;

import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;

public class LockedGradeException extends GradeManagementRuntimeException {
    public LockedGradeException() {
        super("Maingrade is locked and cannot be modified");
    }
}
