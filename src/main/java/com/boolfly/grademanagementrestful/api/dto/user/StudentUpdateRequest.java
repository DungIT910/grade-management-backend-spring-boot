package com.boolfly.grademanagementrestful.api.dto.user;

import com.boolfly.grademanagementrestful.annotation.OUMailChecker;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class StudentUpdateRequest {
    @NotNull
    private String studentId;
    private String firstName;
    private String lastName;
    @OUMailChecker
    @NotBlank
    private String email;
}
