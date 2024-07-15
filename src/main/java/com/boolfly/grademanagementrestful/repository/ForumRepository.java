package com.boolfly.grademanagementrestful.repository;

import com.boolfly.grademanagementrestful.domain.Forum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ForumRepository extends JpaRepository<Forum, Long>, QuerydslPredicateExecutor<Forum> {
}
