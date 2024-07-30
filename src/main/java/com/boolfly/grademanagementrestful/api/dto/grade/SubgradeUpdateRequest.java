package com.boolfly.grademanagementrestful.api.dto.grade;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SubgradeUpdateRequest {
    @NotNull
    private String subcolId;
    @NotNull
    private String studentId;
    private Double grade;

    @Override
    public String toString() {
        return String.format("[%s,%s]:%.2f", subcolId, studentId, grade);
    }
}
