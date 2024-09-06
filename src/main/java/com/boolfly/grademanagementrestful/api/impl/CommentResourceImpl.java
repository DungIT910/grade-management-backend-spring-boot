package com.boolfly.grademanagementrestful.api.impl;

import com.boolfly.grademanagementrestful.api.CommentResource;
import com.boolfly.grademanagementrestful.api.dto.comment.CommentAddRequest;
import com.boolfly.grademanagementrestful.api.dto.comment.CommentResponse;
import com.boolfly.grademanagementrestful.api.dto.comment.CommentUpdateRequest;
import com.boolfly.grademanagementrestful.api.dto.general.PageResponse;
import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;
import com.boolfly.grademanagementrestful.mapper.CommentMapper;
import com.boolfly.grademanagementrestful.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentResourceImpl implements CommentResource {
    private static final CommentMapper commentMapper = CommentMapper.INSTANCE;
    private final CommentService commentService;

    @GetMapping("/{commentId}/immediate-replies")
    @PreAuthorize("@customSecurityExpression.verifyUserInCourseByCommentId(authentication.name, #commentId)")
    @Override
    public PageResponse<CommentResponse> getImmediateReplies(int page, int size, @PathVariable Long commentId) {
        try {
            Page<CommentResponse> pageComment = commentService.getImmediateReplies(page, size, commentId).map(commentMapper::toCommentResponse);
            PageResponse<CommentResponse> pageResponse = new PageResponse<>();
            pageResponse.setContent(pageComment.getContent());
            return pageResponse;
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }

    @PostMapping("/{ancestorId}/")
    @PreAuthorize("@customSecurityExpression.verifyUserInCourseByCommentId(authentication.name, #ancestorId)")
    @Override
    public CommentResponse addReplyToComment(CommentAddRequest request, @PathVariable Long ancestorId) {
        try {
            return commentMapper.toCommentResponse(commentService.addReplytoComment(request, ancestorId));
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }

    @PutMapping("/{commentId}")
    @PreAuthorize("@customSecurityExpression.verifyCommentOwner(authentication.name, #commentId)")
    @Override
    public CommentResponse updateComment(CommentUpdateRequest request, @PathVariable Long commentId) {
        try {
            return commentMapper.toCommentResponse(commentService.updateComment(request, commentId));
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }

    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') " +
            "or (hasRole('ROLE_LECTURER') and @customSecurityExpression.verifyUserInCourseByCommentId(authentication.name, #commentId))" +
            "or @customSecurityExpression.verifyCommentOwner(authentication.name, #commentId)")
    @Override
    public void deleteComment(@PathVariable Long commentId) {
        try {
            commentService.deleteComment(commentId);
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }
}
