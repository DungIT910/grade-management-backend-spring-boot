package com.boolfly.grademanagementrestful.api.dto.comment;

import lombok.Getter;

@Getter
public class SearchCommentRequest {
    private Long commentId;
    private String postId;
}
