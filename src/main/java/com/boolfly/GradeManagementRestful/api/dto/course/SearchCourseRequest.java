package com.boolfly.GradeManagementRestful.api.dto.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.With;

@Getter
@With
@AllArgsConstructor
public class SearchCourseRequest {
    private String courseId;
    private String name;
}
