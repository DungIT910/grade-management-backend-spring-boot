package com.boolfly.grademanagementrestful.api.dto.grade;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GradeResponse {
    private String maingradeId;
    private String studentId;
    private String studentName;
    private Double midtermGrade;
    private Double finalGrade;
    @With
    private List<SubgradeResponse> subgradeList;
    private String courseId;
    private String courseName;
    private String status;
}
