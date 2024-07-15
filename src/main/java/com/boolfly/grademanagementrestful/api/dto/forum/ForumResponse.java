package com.boolfly.grademanagementrestful.api.dto.forum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForumResponse {
    private String forumId;
    private String name;
    private String description;
    private String courseId;
    private String courseName;
    private String status;
}
