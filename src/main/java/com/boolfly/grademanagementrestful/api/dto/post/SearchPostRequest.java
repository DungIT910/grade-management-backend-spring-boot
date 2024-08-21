package com.boolfly.grademanagementrestful.api.dto.post;

import com.boolfly.grademanagementrestful.domain.model.post.PostStatus;
import lombok.Getter;

import java.util.List;

@Getter
public class SearchPostRequest {
    private String postId;
    private String title;
    private String forumId;
    private List<PostStatus> status;
}
