package com.boolfly.grademanagementrestful.api.dto.grade;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MaingradeUpdateRequest {
    @NotNull
    private String maingradeId;
    private boolean updatedMidtermGrade;
    private boolean updateFinalGrade;
    private Double midtermGrade;
    private Double finalGrade;

    @Override
    public String toString() {
        return String.format("[%s]:%.2f|%.2f", maingradeId, midtermGrade, finalGrade);
    }
}
