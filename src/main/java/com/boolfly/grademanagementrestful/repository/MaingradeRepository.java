package com.boolfly.grademanagementrestful.repository;

import com.boolfly.grademanagementrestful.domain.Maingrade;
import com.boolfly.grademanagementrestful.domain.model.maingrade.MaingradeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface MaingradeRepository extends JpaRepository<Maingrade, Long>, QuerydslPredicateExecutor<Maingrade> {
    Optional<Maingrade> findByCourse_IdAndStudent_Id(Long courseId, Long studentId);

    Optional<Maingrade> findByIdAndStatus(Long maingradeId, MaingradeStatus status);

    Optional<Maingrade> findByIdAndStatusNot(Long maingradeId, MaingradeStatus status);

    Optional<Maingrade> findByCourse_IdAndStudent_IdAndStatusNot(Long courseId, Long studentId, MaingradeStatus status);

    List<Maingrade> findAllByCourse_IdAndStatusNot(Long courseId, MaingradeStatus status);

    boolean existsByCourse_IdAndStatusNot(Long courseId, MaingradeStatus status);

    default boolean notExistsByCourse_IdAndStatusNot(Long courseId, MaingradeStatus status) {
        return !existsByCourse_IdAndStatusNot(courseId, status);
    }

    boolean existsByCourse_IdAndStudent_IdAndStatusNot(Long courseId, Long studentId, MaingradeStatus status);

    default boolean notExistsByCourse_IdAndStudent_IdAndStatusNot(Long courseId, Long studentId, MaingradeStatus status) {
        return !existsByCourse_IdAndStudent_IdAndStatusNot(courseId, studentId, status);
    }

    boolean existsByCourse_IdAndStudent_IdAndStatus(Long courseId, Long studentId, MaingradeStatus status);
}
