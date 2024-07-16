package com.boolfly.grademanagementrestful.exception.user;

import com.boolfly.grademanagementrestful.exception.generic.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String id) {
        super("User " + id);
    }
}
