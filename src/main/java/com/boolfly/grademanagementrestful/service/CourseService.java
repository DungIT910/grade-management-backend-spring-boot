package com.boolfly.grademanagementrestful.service;

import com.boolfly.grademanagementrestful.api.dto.course.CourseAddRequest;
import com.boolfly.grademanagementrestful.api.dto.course.CourseUpdateRequest;
import com.boolfly.grademanagementrestful.api.dto.course.SearchCourseRequest;
import com.boolfly.grademanagementrestful.api.dto.course.student.CourseAddStudentRequest;
import com.boolfly.grademanagementrestful.domain.Course;
import com.boolfly.grademanagementrestful.domain.Subcol;
import com.boolfly.grademanagementrestful.domain.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CourseService {
    Page<Course> getCourses(int page, int size, SearchCourseRequest request);

    Course addCourse(CourseAddRequest request);

    Course updateCourse(CourseUpdateRequest request);

    void deactivateCourse(String courseId);

    List<User> addStudentsToCourse(String courseId, CourseAddStudentRequest request);

    void deactivateStudent(String courseId, String studentId);

    List<Subcol> getSubcols(String courseId);
}
