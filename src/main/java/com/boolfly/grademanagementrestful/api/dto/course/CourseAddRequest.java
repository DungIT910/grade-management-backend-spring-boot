package com.boolfly.grademanagementrestful.api.dto.course;

import lombok.Data;

@Data
public class CourseAddRequest {
    private String name;
    private String subjectId;
    private String lecturerId;
}
