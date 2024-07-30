package com.boolfly.grademanagementrestful.api.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LecturerResponse {
    private String lecturerId;
    private String firstName;
    private String lastName;
    private String email;
}
