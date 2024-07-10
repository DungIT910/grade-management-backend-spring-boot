package com.boolfly.GradeManagementRestful.exception.user;

import com.boolfly.GradeManagementRestful.exception.base.GradeManagementRuntimeException;

public class EmailTakenException extends GradeManagementRuntimeException {
    public EmailTakenException(String email) {
        super("Email " + email + " is already taken");
    }
}
