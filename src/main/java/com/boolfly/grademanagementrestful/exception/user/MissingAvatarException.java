package com.boolfly.grademanagementrestful.exception.user;

import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;

public class MissingAvatarException extends GradeManagementRuntimeException {
    public MissingAvatarException() {
        super("The avatar is missing");
    }
}
