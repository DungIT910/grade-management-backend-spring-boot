package com.boolfly.GradeManagementRestful.repository;

import com.boolfly.GradeManagementRestful.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
