package com.boolfly.grademanagementrestful.exception.forum;

import com.boolfly.grademanagementrestful.exception.generic.NotFoundException;

public class ForumNotFoundException extends NotFoundException {
    public ForumNotFoundException(String id) {
        super("Forum " + id);
    }
}
