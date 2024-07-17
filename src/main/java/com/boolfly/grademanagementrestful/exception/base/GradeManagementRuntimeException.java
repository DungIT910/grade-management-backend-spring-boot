package com.boolfly.grademanagementrestful.exception.base;

/**
 * Custom runtime exception for the Grade Management application.
 * This exception is used to indicate errors specific to the Grade Management domain.
 *
 * @author Bao Le
 * @see RuntimeException
 */
public class GradeManagementRuntimeException extends RuntimeException {
    /**
     * Constructs a new {@code GradeManagementRuntimeException} with the specified detail message.
     *
     * @param message the detail message
     */
    public GradeManagementRuntimeException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code GradeManagementRuntimeException} with the specified cause.
     *
     * @param cause the cause of the exception
     */
    public GradeManagementRuntimeException(Throwable cause) {
        super(cause);
    }
}
