package com.boolfly.grademanagementrestful.repository;

import com.boolfly.grademanagementrestful.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SubjectRepository extends JpaRepository<Subject, Long>, QuerydslPredicateExecutor<Subject> {
}
