package com.boolfly.grademanagementrestful.api.dto.grade;

import lombok.Getter;

@Getter
public class SearchGradeRequest {
    private String courseId;
    private String studentId;
}
