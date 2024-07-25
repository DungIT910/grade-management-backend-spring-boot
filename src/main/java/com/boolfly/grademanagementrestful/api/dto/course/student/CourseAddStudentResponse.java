package com.boolfly.grademanagementrestful.api.dto.course.student;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CourseAddStudentResponse {
    private List<CourseStudentResponse> students;
}
