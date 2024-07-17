package com.boolfly.grademanagementrestful.exception.post;

import com.boolfly.grademanagementrestful.exception.generic.NotFoundException;

public class PostNotFoundException extends NotFoundException {
    public PostNotFoundException(String id) {
        super("Post " + id);
    }
}
