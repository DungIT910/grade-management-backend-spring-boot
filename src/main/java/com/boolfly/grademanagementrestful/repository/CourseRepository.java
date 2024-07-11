package com.boolfly.grademanagementrestful.repository;

import com.boolfly.grademanagementrestful.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CourseRepository extends JpaRepository<Course, Long>, QuerydslPredicateExecutor<Course> {
}
