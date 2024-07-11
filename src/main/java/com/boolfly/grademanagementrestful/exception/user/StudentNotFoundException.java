package com.boolfly.grademanagementrestful.exception.user;

import com.boolfly.grademanagementrestful.exception.generic.NotFoundException;

public class StudentNotFoundException extends NotFoundException {
    public StudentNotFoundException() {
        super("Student");
    }
}
