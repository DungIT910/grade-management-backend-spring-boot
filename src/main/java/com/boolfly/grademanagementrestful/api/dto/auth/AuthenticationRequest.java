package com.boolfly.grademanagementrestful.api.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AuthenticationRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
