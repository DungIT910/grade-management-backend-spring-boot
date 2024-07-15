package com.boolfly.grademanagementrestful.api.dto.forum;

import lombok.Getter;

@Getter
public class ForumUpdateRequest {
    private String forumId;
    private String name;
    private String description;
    private String courseId;
}
