package com.boolfly.grademanagementrestful.api.dto.subcol;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubcolResponse {
    private String subcolId;
    private String name;
    private String courseId;
    private String courseName;
    private String status;
}
