package com.boolfly.grademanagementrestful.api.dto.user;

import com.boolfly.grademanagementrestful.annotation.OUMailChecker;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LecturerUpdateRequest implements UserUpdateRequest {
    @NotBlank
    private String id;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @OUMailChecker
    private String email;
}
