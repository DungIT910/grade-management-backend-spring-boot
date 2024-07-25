package com.boolfly.grademanagementrestful.exception.grade;

import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;


public class UpdateErrorSubgradeBatchException extends GradeManagementRuntimeException {
    public UpdateErrorSubgradeBatchException(String listGrade) {
        super("These subgrades have error in update process: " + listGrade);
    }
}
