package com.boolfly.GradeManagementRestful.exception.subject;

import com.boolfly.GradeManagementRestful.exception.generic.NotFoundException;

public class SubjectNotFoundException extends NotFoundException {
    public SubjectNotFoundException(String id) {
        super("Subject " + id);
    }
}
