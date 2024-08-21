package com.boolfly.grademanagementrestful.service;

import com.boolfly.grademanagementrestful.api.dto.comment.CommentAddRequest;
import com.boolfly.grademanagementrestful.api.dto.comment.CommentUpdateRequest;
import com.boolfly.grademanagementrestful.domain.Comment;
import org.springframework.data.domain.Page;

public interface CommentService {
    Comment addCommentToPost(CommentAddRequest request, String postId);

    Comment addReplytoComment(CommentAddRequest request, Long ancestorId);

    Comment updateComment(CommentUpdateRequest request, Long commentId);

    void deleteComment(Long commentId);

    Page<Comment> getCommentsByPostId(int page, int size, String postId);

    Page<Comment> getImmediateReplies(int page, int size, Long commentId);
}
