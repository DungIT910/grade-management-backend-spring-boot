package com.boolfly.grademanagementrestful.api.dto.user;

import com.boolfly.grademanagementrestful.annotation.OUMailChecker;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class StudentUpdateRequest implements UserUpdateRequest {
    @NotBlank
    private String studentId;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @OUMailChecker
    @NotBlank
    private String email;

    @Override
    public String getUserId() {
        return studentId;
    }
}
