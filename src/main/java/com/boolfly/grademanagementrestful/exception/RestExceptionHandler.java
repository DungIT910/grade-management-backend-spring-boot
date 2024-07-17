package com.boolfly.grademanagementrestful.exception;

import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global exception handler for REST controllers.
 * This class handles exceptions thrown by REST controllers and provides appropriate responses.
 *
 * @author Bao Le
 * @see ResponseEntityExceptionHandler
 * @see ExceptionHandler
 * @see RestControllerAdvice
 */
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * Handles {@link GradeManagementRuntimeException} and returns a {@link ProblemDetail} with a BAD_REQUEST status.
     *
     * @param e the exception to be handled
     * @return a {@link ProblemDetail} containing the error details
     */
    @ExceptionHandler(GradeManagementRuntimeException.class)
    public ProblemDetail handleGradeManagementRuntimeException(GradeManagementRuntimeException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    /**
     * Handles {@link MethodArgumentNotValidException} and returns a detailed error response.
     *
     * @param ex      the exception to be handled
     * @param headers the HTTP headers
     * @param status  the HTTP status code
     * @param request the web request
     * @return a {@link ResponseEntity} containing the error details
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ResponseEntity<Object> errorResponse = super.handleMethodArgumentNotValid(ex, headers, status, request);
        if (errorResponse == null) {
            return null;
        }

        StringBuilder errorDetails = new StringBuilder();
        ex.getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errorDetails.append(fieldName).append(' ').append(errorMessage).append(", ");
                });
        errorDetails.setLength(errorDetails.length() - 2);

        ProblemDetail problemDetail = (ProblemDetail) errorResponse.getBody();
        if (problemDetail != null) {
            problemDetail.setDetail(errorDetails.toString());
        }

        return ResponseEntity.badRequest().body(problemDetail);
    }
}
