package com.boolfly.GradeManagementRestful.repository;

import com.boolfly.GradeManagementRestful.domain.Forum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumRepository extends JpaRepository<Forum, Long> {
}
