package com.boolfly.grademanagementrestful.repository;

import com.boolfly.grademanagementrestful.domain.Forum;
import com.boolfly.grademanagementrestful.domain.model.forum.ForumStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface ForumRepository extends JpaRepository<Forum, Long>, QuerydslPredicateExecutor<Forum> {
    Optional<Forum> findByIdAndStatus(Long id, ForumStatus status);

    @Query("SELECT f.course.id FROM Forum f WHERE f.id = :forumId AND f.status = :status")
    Optional<Long> findCourseIdByForumIdAndStatus(Long forumId, ForumStatus status);
}
