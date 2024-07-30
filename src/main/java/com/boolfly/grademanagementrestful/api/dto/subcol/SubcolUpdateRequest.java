package com.boolfly.grademanagementrestful.api.dto.subcol;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SubcolUpdateRequest {
    @NotNull
    private String subcolId;
    private String subcolName;
}
