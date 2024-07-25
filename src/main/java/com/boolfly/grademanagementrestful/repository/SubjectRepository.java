package com.boolfly.grademanagementrestful.repository;

import com.boolfly.grademanagementrestful.domain.Subject;
import com.boolfly.grademanagementrestful.domain.model.subject.SubjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long>, QuerydslPredicateExecutor<Subject> {
    Optional<Subject> findByIdAndStatus(Long id, SubjectStatus status);
}
