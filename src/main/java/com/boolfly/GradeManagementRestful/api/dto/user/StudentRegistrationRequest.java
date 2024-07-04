package com.boolfly.GradeManagementRestful.api.dto.user;

import com.boolfly.GradeManagementRestful.annotation.OUMailChecker;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StudentRegistrationRequest {
    private String firstName;
    private String lastName;

    @OUMailChecker
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
