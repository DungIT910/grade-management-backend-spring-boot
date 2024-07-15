package com.boolfly.grademanagementrestful.repository;

import com.boolfly.grademanagementrestful.domain.Forum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumRepository extends JpaRepository<Forum, Long> {
}
