package com.boolfly.grademanagementrestful.api.dto.user;

import com.boolfly.grademanagementrestful.annotation.OUMailChecker;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LecturerAddRequest implements UserRegistrationRequest {
    private String firstName;
    private String lastName;

    @OUMailChecker
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
