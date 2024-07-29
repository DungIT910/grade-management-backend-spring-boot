package com.boolfly.grademanagementrestful.api.dto.user;

import com.boolfly.grademanagementrestful.annotation.OUMailChecker;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LecturerUpdateRequest implements UserUpdateRequest {
    @NotBlank
    private String lecturerId;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @OUMailChecker
    private String email;

    @Override
    @JsonIgnore
    public String getUserId() {
        return lecturerId;
    }
}
