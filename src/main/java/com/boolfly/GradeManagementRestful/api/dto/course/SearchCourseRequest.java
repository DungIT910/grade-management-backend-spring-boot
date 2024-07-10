package com.boolfly.GradeManagementRestful.api.dto.course;

import com.boolfly.GradeManagementRestful.domain.model.course.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.With;

import java.util.List;

@Getter
@With
@AllArgsConstructor
public class SearchCourseRequest {
    private String courseId;
    private String name;
    private List<CourseStatus> status;
}
