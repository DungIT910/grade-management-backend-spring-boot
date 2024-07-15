package com.boolfly.grademanagementrestful.service;

import com.boolfly.grademanagementrestful.api.dto.forum.ForumAddRequest;
import com.boolfly.grademanagementrestful.api.dto.forum.ForumUpdateRequest;
import com.boolfly.grademanagementrestful.api.dto.forum.SearchForumRequest;
import com.boolfly.grademanagementrestful.domain.Forum;
import org.springframework.data.domain.Page;

public interface ForumService {
    Page<Forum> getForums(int page, int size, SearchForumRequest request);

    Forum addForum(ForumAddRequest request);

    Forum updateForum(ForumUpdateRequest request);

    void deactivateForum(String forumId);
}
