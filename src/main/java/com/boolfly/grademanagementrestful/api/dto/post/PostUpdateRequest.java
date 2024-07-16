package com.boolfly.grademanagementrestful.api.dto.post;

import lombok.Getter;

@Getter
public class PostUpdateRequest {
    private String postId;
    private String title;
    private String content;
    private String forumId;
    private String userId;
}
