package com.boolfly.grademanagementrestful.service;

import com.boolfly.grademanagementrestful.api.dto.post.PostAddRequest;
import com.boolfly.grademanagementrestful.api.dto.post.PostUpdateRequest;
import com.boolfly.grademanagementrestful.api.dto.post.SearchPostRequest;
import com.boolfly.grademanagementrestful.domain.Post;
import org.springframework.data.domain.Page;

public interface PostService {
    Page<Post> getPosts(int page, int size, SearchPostRequest request);

    Post addPost(PostAddRequest request);

    Post updatePost(PostUpdateRequest request);

    void deactivatePost(String postId);
}
