package com.boolfly.grademanagementrestful.repository;

import com.boolfly.grademanagementrestful.domain.Comment;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, QuerydslPredicateExecutor<Comment> {
    boolean existsById(@NonNull Long id);

    @Query("SELECT c.post.forum.course.id FROM Comment c WHERE c.id = :commentId")
    Optional<Long> findCourseIdByCommentId(Long commentId);

    @Query("SELECT c FROM Comment c WHERE NOT EXISTS (SELECT 1 FROM c.ancestorRelations a WHERE a.depth <> 0) AND c.post.id = :postId")
    Page<Comment> findAllRootCommentsByPost_Id(Pageable pageable, Long postId);

    @Query("SELECT c FROM Comment c JOIN c.ancestorRelations a WHERE a.ancestor.id = :ancestorId AND a.depth = 1")
    Page<Comment> findAllImmediateDescendants(Pageable pageable, Long ancestorId);

    @Query("SELECT EXISTS (SELECT 1 FROM Comment c WHERE c.id = :commentId AND c.createdBy.email = :email)")
    Boolean isCommentOwner(Long commentId, String email);
}


