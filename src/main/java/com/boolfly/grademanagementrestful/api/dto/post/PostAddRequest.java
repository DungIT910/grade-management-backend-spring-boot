package com.boolfly.grademanagementrestful.api.dto.post;

import lombok.Getter;

@Getter
public class PostAddRequest {
    private String title;
    private String content;
    private String forumId;
    private String userId;
}
