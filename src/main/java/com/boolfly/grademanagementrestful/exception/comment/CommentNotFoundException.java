package com.boolfly.grademanagementrestful.exception.comment;

import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;

public class CommentNotFoundException extends GradeManagementRuntimeException {
    public CommentNotFoundException() {
        super("Comment not found");
    }
}
