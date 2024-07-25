package com.boolfly.grademanagementrestful.api.dto.course.student;

import lombok.Getter;

import java.util.List;

@Getter
public class CourseAddStudentRequest {
    List<String> studentIds;
}
