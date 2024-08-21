package com.boolfly.grademanagementrestful.api.dto.post;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PostUpdateRequest {
    @NotNull
    private String postId;
    private String title;
    private String content;
}
