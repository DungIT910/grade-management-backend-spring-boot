package com.boolfly.GradeManagementRestful.api.impl;

import com.boolfly.GradeManagementRestful.api.CourseResource;
import com.boolfly.GradeManagementRestful.api.dto.course.CourseResponse;
import com.boolfly.GradeManagementRestful.api.dto.course.SearchCourseRequest;
import com.boolfly.GradeManagementRestful.api.dto.general.PageResponse;
import com.boolfly.GradeManagementRestful.mapper.CourseMapper;
import com.boolfly.GradeManagementRestful.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
