package com.boolfly.grademanagementrestful.exception.user;

import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;

public class AvatarUploadException extends GradeManagementRuntimeException {
    public AvatarUploadException() {
        super("Avatar failed to upload");
    }
}
