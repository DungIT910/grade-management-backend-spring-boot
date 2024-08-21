package com.boolfly.grademanagementrestful.api.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentAddRequest {
    @NotBlank
    private String content;
}
