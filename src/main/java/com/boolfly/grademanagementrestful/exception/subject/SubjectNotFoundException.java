package com.boolfly.grademanagementrestful.exception.subject;

import com.boolfly.grademanagementrestful.exception.generic.NotFoundException;

public class SubjectNotFoundException extends NotFoundException {
    public SubjectNotFoundException(String id) {
        super("Subject " + id);
    }
}
