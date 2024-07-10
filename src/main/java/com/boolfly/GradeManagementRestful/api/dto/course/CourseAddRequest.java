package com.boolfly.GradeManagementRestful.api.dto.course;

import lombok.Data;

@Data
public class CourseAddRequest {
    private String name;
    private String subjectId;
    private String lecturerId;
}
