package com.boolfly.grademanagementrestful.repository;

import com.boolfly.grademanagementrestful.domain.Course;
import com.boolfly.grademanagementrestful.domain.model.course.CourseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long>, QuerydslPredicateExecutor<Course> {
    Optional<Course> findByIdAndStatus(Long id, CourseStatus status);

    @Query("SELECT c.name FROM Course c WHERE c.id = ?1 AND c.status = ?2")
    Optional<String> findNameByIdAndStatus(Long id, CourseStatus status);

    boolean existsByIdAndStatus(Long id, CourseStatus status);

    default boolean notExistsByIdAndStatus(Long id, CourseStatus status) {
        return !existsByIdAndStatus(id, status);
    }
}
