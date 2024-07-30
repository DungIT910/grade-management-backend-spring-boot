package com.boolfly.grademanagementrestful.api.dto.user;

public interface UserRegistrationRequest {
    String getFirstName();

    String getLastName();

    String getEmail();

    String getPassword();
}
