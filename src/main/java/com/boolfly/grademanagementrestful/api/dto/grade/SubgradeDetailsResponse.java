package com.boolfly.grademanagementrestful.api.dto.grade;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubgradeDetailsResponse {
    private String subgradeId;
    private String subcolId;
    private String subcolName;
    private String studentId;
    private String studentName;
    private Double grade;
    private String courseId;
    private String courseName;
}
