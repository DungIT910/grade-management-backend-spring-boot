package com.boolfly.GradeManagementRestful.api.dto.course;

import lombok.Data;

@Data
public class CourseResponse {
    private String courseId;
    private String name;
    private String subjectId;
    private String lecturerId;
}
