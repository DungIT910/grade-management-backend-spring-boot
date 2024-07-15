package com.boolfly.grademanagementrestful.api.dto.forum;

import lombok.Getter;

@Getter
public class ForumAddRequest {
    private String name;
    private String description;
    private String courseId;
}
