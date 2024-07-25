package com.boolfly.grademanagementrestful.api.dto.course;

import lombok.Getter;

@Getter
public class CourseAddRequest {
    private String name;
    private String subjectId;
    private String lecturerId;
}
