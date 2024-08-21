package com.boolfly.grademanagementrestful.repository;

import com.boolfly.grademanagementrestful.domain.Post;
import com.boolfly.grademanagementrestful.domain.model.post.PostStatus;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, QuerydslPredicateExecutor<Post> {
    List<Post> findAllByForum_Id(Long forumId);

    Optional<Post> findByIdAndStatus(Long id, PostStatus status);

    boolean existsById(@NonNull Long id);

    default boolean notExistById(Long id) {
        return !existsById(id);
    }
}
