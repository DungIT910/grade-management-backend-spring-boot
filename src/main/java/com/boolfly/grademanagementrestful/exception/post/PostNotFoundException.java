package com.boolfly.grademanagementrestful.exception.post;

import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;

public class PostNotFoundException extends GradeManagementRuntimeException {
    public PostNotFoundException(String id) {
        super("Post " + id + " not found");
    }
}
