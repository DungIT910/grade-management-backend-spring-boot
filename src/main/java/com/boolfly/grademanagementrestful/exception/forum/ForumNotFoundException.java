package com.boolfly.grademanagementrestful.exception.forum;

import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;

public class ForumNotFoundException extends GradeManagementRuntimeException {
    public ForumNotFoundException(String id) {
        super("Forum " + id + " not found");
    }
}
