package com.boolfly.GradeManagementRestful.exception.course;

import com.boolfly.GradeManagementRestful.exception.generic.NotFoundException;

public class CourseNotFoundException extends NotFoundException {
    public CourseNotFoundException(String id) {
        super("Course " + id);
    }
}
