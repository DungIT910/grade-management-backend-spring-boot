package com.boolfly.grademanagementrestful.exception.course;

import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;

public class CourseNotFoundException extends GradeManagementRuntimeException {
    public CourseNotFoundException(String id) {
        super("Course " + id + " not found");
    }
}
