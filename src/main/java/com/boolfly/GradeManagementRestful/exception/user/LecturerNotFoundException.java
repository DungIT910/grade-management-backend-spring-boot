package com.boolfly.GradeManagementRestful.exception.user;

import com.boolfly.GradeManagementRestful.exception.generic.NotFoundException;

public class LecturerNotFoundException extends NotFoundException {
    public LecturerNotFoundException(String id) {
        super("Lecturer " + id);
    }
}
