package com.boolfly.grademanagementrestful.repository;

import com.boolfly.grademanagementrestful.domain.Subcol;
import com.boolfly.grademanagementrestful.domain.model.subcol.SubcolStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface SubcolRepository extends JpaRepository<Subcol, Long>, QuerydslPredicateExecutor<Subcol> {
    Long countByCourseIdAndStatus(Long courseId, SubcolStatus status);

    List<Subcol> findAllByCourse_IdAndStatus(Long courseId, SubcolStatus status);

    Optional<Subcol> findByIdAndStatus(Long id, SubcolStatus status);

    @Query("SELECT s.course.id FROM Subcol s WHERE s.id = :subcolId AND s.status = :status")
    Optional<Long> findCourseIdBySubcol_IdAndStatus(Long subcolId, SubcolStatus status);
}

