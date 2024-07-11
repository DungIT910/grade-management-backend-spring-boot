package com.boolfly.grademanagementrestful.api.dto.course;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CourseResponse {
    private String courseId;
    private String name;
    private String subjectId;
    private String subjectName;
    private String lecturerId;
    private String lecturerName;
    private LocalDate startTime;
    private LocalDate endTime;
    private Integer minQuantity;
    private String status;
}
