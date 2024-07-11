package com.boolfly.grademanagementrestful.exception;

import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(GradeManagementRuntimeException.class)
    public ProblemDetail handleGradeManagementRuntimeException(GradeManagementRuntimeException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

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
