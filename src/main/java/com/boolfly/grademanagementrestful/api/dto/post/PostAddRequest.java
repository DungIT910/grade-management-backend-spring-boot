package com.boolfly.grademanagementrestful.api.dto.post;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PostAddRequest {
    private String title;
    private String content;
    @NotNull
    private String forumId;
    @NotNull
    private String userId;
}
