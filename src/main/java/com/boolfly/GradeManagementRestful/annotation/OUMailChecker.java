package com.boolfly.GradeManagementRestful.annotation;

import com.boolfly.GradeManagementRestful.annotation.validator.OUCheckerValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OUCheckerValidation.class)
public @interface OUMailChecker {
    String message() default "Mail must contains ou.edu.vn";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
