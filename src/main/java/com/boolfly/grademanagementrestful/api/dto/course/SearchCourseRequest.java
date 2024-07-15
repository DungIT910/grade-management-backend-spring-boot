package com.boolfly.grademanagementrestful.api.dto.course;

import com.boolfly.grademanagementrestful.domain.model.course.CourseStatus;
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
