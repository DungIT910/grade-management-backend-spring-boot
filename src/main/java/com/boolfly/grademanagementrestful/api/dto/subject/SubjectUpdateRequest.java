package com.boolfly.grademanagementrestful.api.dto.subject;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SubjectUpdateRequest {
    @NotNull
    private String subjectId;
    private String name;
}
