package com.boolfly.grademanagementrestful.api.dto.grade;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SubgradeAddRequest {
    @NotNull
    private String subcolId;
    @NotNull
    private String studentId;
    private String grade;
}
