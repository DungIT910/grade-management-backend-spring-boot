package com.boolfly.GradeManagementRestful.annotation.validator;

import com.boolfly.GradeManagementRestful.annotation.OUMailChecker;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OUCheckerValidation implements ConstraintValidator<OUMailChecker, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.endsWith("@ou.edu.vn");
    }
}
