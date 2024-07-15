package com.boolfly.grademanagementrestful.api.dto.user;

import lombok.Data;

@Data
public class StudentResponse {
    private String studentId;
    private String firstName;
    private String lastName;
    private String email;
}
