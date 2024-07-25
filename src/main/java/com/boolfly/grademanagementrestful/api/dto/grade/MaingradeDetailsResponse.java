package com.boolfly.grademanagementrestful.api.dto.grade;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaingradeDetailsResponse {
    private String maingradeId;
    private String studentId;
    private String studentName;
    private Double midtermGrade;
    private Double finalGrade;
    private String courseId;
    private String courseName;
}
