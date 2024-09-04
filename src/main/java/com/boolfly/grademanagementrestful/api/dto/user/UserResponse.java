package com.boolfly.grademanagementrestful.api.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String avatar;
    private String role;
}
