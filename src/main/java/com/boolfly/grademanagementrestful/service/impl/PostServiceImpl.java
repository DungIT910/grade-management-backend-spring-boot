package com.boolfly.grademanagementrestful.service.impl;

import com.boolfly.grademanagementrestful.api.dto.post.PostAddRequest;
import com.boolfly.grademanagementrestful.api.dto.post.PostUpdateRequest;
import com.boolfly.grademanagementrestful.api.dto.post.SearchPostRequest;
import com.boolfly.grademanagementrestful.builder.base.SearchParamsBuilder;
import com.boolfly.grademanagementrestful.builder.post.PostSearchParamsBuilder;
import com.boolfly.grademanagementrestful.domain.Post;
import com.boolfly.grademanagementrestful.domain.model.forum.ForumStatus;
import com.boolfly.grademanagementrestful.domain.model.post.PostStatus;
import com.boolfly.grademanagementrestful.exception.forum.ForumNotFoundException;
import com.boolfly.grademanagementrestful.exception.post.PostNotFoundException;
import com.boolfly.grademanagementrestful.repository.ForumRepository;
import com.boolfly.grademanagementrestful.repository.PostRepository;
import com.boolfly.grademanagementrestful.service.PostService;
import com.querydsl.core.types.dsl.BooleanExpression;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final ForumRepository forumRepository;

    @Override
    public Page<Post> getPosts(int page, int size, SearchPostRequest request) {
        SearchParamsBuilder paramsBuilder = PostSearchParamsBuilder.from(page, size, request);
        Optional<BooleanExpression> predicate = paramsBuilder.getCommonCriteria();
        Pageable pageable = paramsBuilder.getPageable();
        return predicate.map(pre -> postRepository.findAll(pre, pageable))
                .orElseGet(() -> postRepository.findAll(pageable));
    }

    @Override
    public Post addPost(PostAddRequest request) {
        return forumRepository.findByIdAndStatus(TSID.from(request.getForumId()).toLong(), ForumStatus.ACTIVE)
                .map(forum -> postRepository.save(Post.builder()
                        .id(TSID.fast().toLong())
                        .title(request.getTitle())
                        .content(request.getContent())
                        .forum(forum)
                        .status(PostStatus.ACTIVE)
                        .build())
                ).orElseThrow(() -> new ForumNotFoundException(request.getForumId()));
    }

    @Override
    @PostAuthorize("returnObject.createdBy.email == authentication.name")
    public Post updatePost(PostUpdateRequest request) {
        TSID postId = TSID.from(request.getPostId());

        Post post = postRepository.findByIdAndStatus(postId.toLong(), PostStatus.ACTIVE)
                .orElseThrow(() -> new PostNotFoundException(request.getPostId()));

        Optional.ofNullable(request.getTitle())
                .filter(title -> !title.isEmpty() && !Objects.equals(post.getTitle(), title))
                .ifPresent(post::setTitle);

        Optional.ofNullable(request.getContent())
                .filter(content -> !content.isEmpty() && !Objects.equals(post.getContent(), content))
                .ifPresent(post::setContent);

        return postRepository.save(post);
    }

    @Override
    @PostAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_LECTURER') or returnObject.createdBy.email == authentication.name")
    public void deactivatePost(String postId) {
        Post post = postRepository.findByIdAndStatus(TSID.from(postId).toLong(), PostStatus.ACTIVE)
                .orElseThrow(() -> new PostNotFoundException(postId));

        post.setStatus(PostStatus.INACTIVE);
        postRepository.save(post);
    }
}
