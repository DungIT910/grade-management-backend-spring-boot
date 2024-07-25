package com.boolfly.grademanagementrestful.repository;

import com.boolfly.grademanagementrestful.domain.Course;
import com.boolfly.grademanagementrestful.domain.model.course.CourseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long>, QuerydslPredicateExecutor<Course> {
    Optional<Course> findByIdAndStatus(Long id, CourseStatus status);
}
