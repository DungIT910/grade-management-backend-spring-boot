package com.boolfly.grademanagementrestful.api.dto.subcol;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CourseSubcolResponse {
    private List<SubcolResponse> subcols;
}
