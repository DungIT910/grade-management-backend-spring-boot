package com.boolfly.GradeManagementRestful.api;

import com.boolfly.GradeManagementRestful.api.dto.course.CourseResponse;
import com.boolfly.GradeManagementRestful.api.dto.course.SearchCourseRequest;
import com.boolfly.GradeManagementRestful.api.dto.general.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Course Resource")
@Validated
public interface CourseResource {
    @Operation(summary = "Get all courses")
    PageResponse<CourseResponse> getCourses(@RequestParam(defaultValue = "0") @Min(0) int page,
                                            @RequestParam(defaultValue = "10") @Min(1) int size,
                                            @RequestBody SearchCourseRequest request);
}
