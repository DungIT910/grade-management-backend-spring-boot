package com.boolfly.grademanagementrestful.repository;

import com.boolfly.grademanagementrestful.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
