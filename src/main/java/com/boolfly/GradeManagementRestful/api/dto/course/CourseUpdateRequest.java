package com.boolfly.GradeManagementRestful.api.dto.course;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CourseUpdateRequest {
    @NotNull
    private String courseId;
    private String name;
    private String subjectId;
    private String lecturerId;
    @NotNull
    private LocalDate startTime;
    @NotNull
    private LocalDate endTime;
    @NotNull
    @Min(1)
    private Integer minQuantity;
}
