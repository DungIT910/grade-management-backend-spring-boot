package com.boolfly.grademanagementrestful.api.dto.grade;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MaingradeUpdateRequest {
    @NotNull
    private String maingradeId;
    private Double midtermGrade;
    private Double finalGrade;
    @JsonProperty(defaultValue = "false")
    private boolean locked;

    @Override
    public String toString() {
        return String.format("[%s]:%.2f|%.2f", maingradeId, midtermGrade, finalGrade);
    }
}
