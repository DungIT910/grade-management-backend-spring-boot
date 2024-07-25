package com.boolfly.grademanagementrestful.api;

import com.boolfly.grademanagementrestful.api.dto.course.CourseAddRequest;
import com.boolfly.grademanagementrestful.api.dto.course.CourseResponse;
import com.boolfly.grademanagementrestful.api.dto.course.CourseUpdateRequest;
import com.boolfly.grademanagementrestful.api.dto.course.SearchCourseRequest;
import com.boolfly.grademanagementrestful.api.dto.course.student.CourseAddStudentRequest;
import com.boolfly.grademanagementrestful.api.dto.course.student.CourseAddStudentResponse;
import com.boolfly.grademanagementrestful.api.dto.general.PageResponse;
import com.boolfly.grademanagementrestful.api.dto.subcol.CourseSubcolResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

    @Operation(summary = "add new course")
    CourseResponse addCourse(@Valid @RequestBody CourseAddRequest request);

    @Operation(summary = "update a course")
    CourseResponse updateCourse(@Valid @RequestBody CourseUpdateRequest request);

    @Operation(summary = "deactivate a course")
    void deactivateCourse(String courseId);

    @Operation(summary = "Add students to a course")
    CourseAddStudentResponse addStudentsToCourse(String courseId, @Valid @RequestBody CourseAddStudentRequest request);

    @Operation(summary = "Deactivate a student in a course")
    void deactivateStudent(String courseId, String studentId);

    @Operation(summary = "Get all sub-cols of a course")
    CourseSubcolResponse getSubcols(String courseId);
}
