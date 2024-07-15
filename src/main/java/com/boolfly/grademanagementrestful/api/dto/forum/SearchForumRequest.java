package com.boolfly.grademanagementrestful.api.dto.forum;

import com.boolfly.grademanagementrestful.domain.model.forum.ForumStatus;
import lombok.Getter;

import java.util.List;

@Getter
public class SearchForumRequest {
    private String forumId;
    private String name;
    private List<ForumStatus> status;
}
