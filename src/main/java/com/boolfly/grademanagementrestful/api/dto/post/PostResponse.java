package com.boolfly.grademanagementrestful.api.dto.post;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private String status;
}
