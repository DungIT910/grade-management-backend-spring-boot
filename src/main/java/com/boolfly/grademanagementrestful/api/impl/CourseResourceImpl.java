package com.boolfly.grademanagementrestful.api.impl;

import com.boolfly.grademanagementrestful.api.CourseResource;
import com.boolfly.grademanagementrestful.api.dto.course.CourseAddRequest;
import com.boolfly.grademanagementrestful.api.dto.course.CourseResponse;
import com.boolfly.grademanagementrestful.api.dto.course.CourseUpdateRequest;
import com.boolfly.grademanagementrestful.api.dto.course.SearchCourseRequest;
import com.boolfly.grademanagementrestful.api.dto.general.PageResponse;
import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;
import com.boolfly.grademanagementrestful.mapper.CourseMapper;
import com.boolfly.grademanagementrestful.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseResourceImpl implements CourseResource {
    private static final CourseMapper courseMapper = CourseMapper.INSTANCE;
    private final CourseService courseService;

    @PostMapping("/search")
    @Override
    public PageResponse<CourseResponse> getCourses(int page, int size, SearchCourseRequest request) {
        Page<CourseResponse> pageCourse = courseService.getCourses(page, size, request)
                .map(courseMapper::toCourseResponse);
        PageResponse<CourseResponse> pageCourseResponse = new PageResponse<>();
        pageCourseResponse.setContent(pageCourse.getContent());
        return pageCourseResponse;
    }

    @PostMapping
    @Override
    public CourseResponse addCourse(CourseAddRequest request) {
        return courseMapper.toCourseResponse(courseService.addCourse(request));
    }

    @PutMapping
    @Override
    public CourseResponse updateCourse(CourseUpdateRequest request) {
        try {
            return courseMapper.toCourseResponse(courseService.updateCourse(request));
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }

    @DeleteMapping("/{courseId}")
    @Override
    public void deactivateCourse(@PathVariable String courseId) {
        courseService.deactivateCourse(courseId);
    }
}
