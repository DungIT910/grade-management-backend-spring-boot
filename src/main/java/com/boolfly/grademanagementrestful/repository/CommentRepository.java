package com.boolfly.grademanagementrestful.repository;

import com.boolfly.grademanagementrestful.domain.Comment;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CommentRepository extends JpaRepository<Comment, Long>, QuerydslPredicateExecutor<Comment> {
    boolean existsById(@NonNull Long id);

    default boolean notExistById(Long id) {
        return !existsById(id);
    }

    @Query("SELECT c FROM Comment c WHERE NOT EXISTS (SELECT 1 FROM c.ancestorRelations a WHERE a.depth <> 0) AND c.post.id = :postId")
    Page<Comment> findAllRootCommentsByPost_Id(Pageable pageable, Long postId);

    @Query("SELECT c FROM Comment c JOIN c.ancestorRelations a WHERE a.ancestor.id = :ancestorId AND a.depth = 1")
    Page<Comment> findAllImmediateDescendants(Pageable pageable, Long ancestorId);
}


