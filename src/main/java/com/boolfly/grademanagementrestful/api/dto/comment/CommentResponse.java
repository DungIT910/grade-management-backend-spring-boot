package com.boolfly.grademanagementrestful.api.dto.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponse {
    private Long commentId;
    private String content;
    private String createdById;
    private String createdByName;
    private String updatedById;
    private String updatedByName;
    private String createdOn;
    private String updatedOn;
    private String postId;
    private String postTitle;
}
