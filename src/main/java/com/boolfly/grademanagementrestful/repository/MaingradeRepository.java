package com.boolfly.grademanagementrestful.repository;

import com.boolfly.grademanagementrestful.domain.Maingrade;
import com.boolfly.grademanagementrestful.domain.model.maingrade.MaingradeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface MaingradeRepository extends JpaRepository<Maingrade, Long>, QuerydslPredicateExecutor<Maingrade> {
    Optional<Maingrade> findByCourse_IdAndStudent_Id(Long courseId, Long studentId);

    Optional<Maingrade> findByCourse_IdAndStudent_IdAndStatus(Long courseId, Long studentId, MaingradeStatus status);

    Optional<Maingrade> findByIdAndStatus(Long maingradeId, MaingradeStatus status);

    List<Maingrade> findAllByCourse_IdAndStatus(Long courseId, MaingradeStatus status);
}
