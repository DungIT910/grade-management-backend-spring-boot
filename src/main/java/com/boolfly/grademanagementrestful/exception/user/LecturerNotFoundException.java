package com.boolfly.grademanagementrestful.exception.user;

import com.boolfly.grademanagementrestful.exception.generic.NotFoundException;

public class LecturerNotFoundException extends NotFoundException {
    public LecturerNotFoundException() {
        super("Lecturer");
    }
}
