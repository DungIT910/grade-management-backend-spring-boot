package com.boolfly.grademanagementrestful.api.dto.course.student;

import com.boolfly.grademanagementrestful.api.dto.general.CollectionResponse;
import lombok.Builder;

import java.util.Collection;

public class CourseAddStudentResponse extends CollectionResponse<CourseStudentResponse> {
    @Builder
    public CourseAddStudentResponse(Collection<CourseStudentResponse> content) {
        super(content);
    }
}
