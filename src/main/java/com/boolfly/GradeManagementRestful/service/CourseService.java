package com.boolfly.GradeManagementRestful.service;

import com.boolfly.GradeManagementRestful.api.dto.course.CourseAddRequest;
import com.boolfly.GradeManagementRestful.api.dto.course.CourseUpdateRequest;
import com.boolfly.GradeManagementRestful.api.dto.course.SearchCourseRequest;
import com.boolfly.GradeManagementRestful.domain.Course;
import org.springframework.data.domain.Page;

public interface CourseService {
    Page<Course> getCourses(int page, int size, SearchCourseRequest request);

    Course addCourse(CourseAddRequest request);

    Course updateCourse(CourseUpdateRequest request);

    void deactivateCourse(String courseId);
}
