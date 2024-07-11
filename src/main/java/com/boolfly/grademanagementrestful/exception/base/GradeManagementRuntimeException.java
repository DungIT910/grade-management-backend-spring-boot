package com.boolfly.grademanagementrestful.exception.base;

public class GradeManagementRuntimeException extends RuntimeException {
    public GradeManagementRuntimeException(String message) {
        super(message);
    }

    public GradeManagementRuntimeException(Throwable cause) {
        super(cause);
    }
}
