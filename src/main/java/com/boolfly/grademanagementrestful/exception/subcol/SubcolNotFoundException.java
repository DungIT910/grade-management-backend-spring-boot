package com.boolfly.grademanagementrestful.exception.subcol;

import com.boolfly.grademanagementrestful.exception.generic.NotFoundException;

public class SubcolNotFoundException extends NotFoundException {
    public SubcolNotFoundException(String id) {
        super("Subcol " + id);
    }
}
