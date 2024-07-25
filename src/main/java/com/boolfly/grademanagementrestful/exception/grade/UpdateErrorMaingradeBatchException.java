package com.boolfly.grademanagementrestful.exception.grade;

import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;

public class UpdateErrorMaingradeBatchException extends GradeManagementRuntimeException {
    public UpdateErrorMaingradeBatchException(String listGrade) {
        super("These maingrades have error in update process: " + listGrade);
    }
}
