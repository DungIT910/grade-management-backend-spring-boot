package com.boolfly.grademanagementrestful.api.impl;

import com.boolfly.grademanagementrestful.api.PostResource;
import com.boolfly.grademanagementrestful.api.dto.general.PageResponse;
import com.boolfly.grademanagementrestful.api.dto.post.PostAddRequest;
import com.boolfly.grademanagementrestful.api.dto.post.PostResponse;
import com.boolfly.grademanagementrestful.api.dto.post.PostUpdateRequest;
import com.boolfly.grademanagementrestful.api.dto.post.SearchPostRequest;
import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;
import com.boolfly.grademanagementrestful.mapper.PostMapper;
import com.boolfly.grademanagementrestful.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostResourceImpl implements PostResource {
    private static final PostMapper postMapper = PostMapper.INSTANCE;
    private final PostService postService;

    @PostMapping("/search")
    @Override
    public PageResponse<PostResponse> getPosts(int page, int size, SearchPostRequest request) {
        try {
            Page<PostResponse> pagePost = postService.getPosts(page, size, request).map(postMapper::toPostResponse);
            PageResponse<PostResponse> pageResponse = new PageResponse<>();
            pageResponse.setContent(pagePost.getContent());
            return pageResponse;
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }

    @PostMapping
    @Override
    public PostResponse addPost(PostAddRequest request) {
        try {
            return postMapper.toPostResponse(postService.addPost(request));
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }

    @PutMapping
    @Override
    public PostResponse updatePost(PostUpdateRequest request) {
        try {
            return postMapper.toPostResponse(postService.updatePost(request));
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }

    @DeleteMapping("/{postId}")
    @Override
    public void deactivatePost(@PathVariable String postId) {
        try {
            postService.deactivatePost(postId);
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }
}