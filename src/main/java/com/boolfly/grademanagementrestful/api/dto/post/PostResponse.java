package com.boolfly.grademanagementrestful.api.dto.post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponse {
    private String postId;
    private String title;
    private String content;
    private String forumId;
    private String forumName;
    private String userId;
    private String userName;
    private String createdOn;
    private String updatedOn;
    private String status;
}
