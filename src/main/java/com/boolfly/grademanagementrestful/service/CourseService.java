package com.boolfly.grademanagementrestful.service;

import com.boolfly.grademanagementrestful.api.dto.course.CourseAddRequest;
import com.boolfly.grademanagementrestful.api.dto.course.CourseUpdateRequest;
import com.boolfly.grademanagementrestful.api.dto.course.SearchCourseRequest;
import com.boolfly.grademanagementrestful.domain.Course;
import org.springframework.data.domain.Page;

public interface CourseService {
    Page<Course> getCourses(int page, int size, SearchCourseRequest request);

    Course addCourse(CourseAddRequest request);

    Course updateCourse(CourseUpdateRequest request);

    void deactivateCourse(String courseId);
}
