package com.boolfly.grademanagementrestful.api;

import com.boolfly.grademanagementrestful.api.dto.comment.CommentAddRequest;
import com.boolfly.grademanagementrestful.api.dto.comment.CommentResponse;
import com.boolfly.grademanagementrestful.api.dto.comment.CommentUpdateRequest;
import com.boolfly.grademanagementrestful.api.dto.general.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Comment Resource")
@Validated
public interface CommentResource {
    @Operation(summary = "Get direct replies to a specific comment")
    PageResponse<CommentResponse> getImmediateReplies(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                      @RequestParam(defaultValue = "10") @Min(1) int size,
                                                      Long commentId);

    @Operation(summary = "Add a reply to a comment")
    CommentResponse addReplyToComment(@RequestBody CommentAddRequest request, Long ancestorId);

    @Operation(summary = "Update a comment")
    CommentResponse updateComment(@Valid @RequestBody CommentUpdateRequest request, Long commentId);

    @Operation(summary = "Delete a comment")
    void deleteComment(Long commentId);
}
