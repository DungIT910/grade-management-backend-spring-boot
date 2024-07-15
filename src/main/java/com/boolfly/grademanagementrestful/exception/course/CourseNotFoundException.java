package com.boolfly.grademanagementrestful.exception.course;

import com.boolfly.grademanagementrestful.exception.generic.NotFoundException;

public class CourseNotFoundException extends NotFoundException {
    public CourseNotFoundException(String id) {
        super("Course " + id);
    }
}
