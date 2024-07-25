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
import com.boolfly.grademanagementrestful.exception.user.UserNotFoundException;
import com.boolfly.grademanagementrestful.repository.ForumRepository;
import com.boolfly.grademanagementrestful.repository.PostRepository;
import com.boolfly.grademanagementrestful.repository.UserRepository;
import com.boolfly.grademanagementrestful.service.PostService;
import com.querydsl.core.types.dsl.BooleanExpression;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final ForumRepository forumRepository;
    private final UserRepository userRepository;

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
                .map(forum -> userRepository.findByIdAndActiveTrue(TSID.from(request.getUserId()).toLong())
                        .map(user -> postRepository.save(Post.builder()
                                .id(TSID.fast().toLong())
                                .title(request.getTitle())
                                .content(request.getContent())
                                .forum(forum)
                                .user(user)
                                .status(PostStatus.ACTIVE)
                                .build()))
                        .orElseThrow(() -> new UserNotFoundException(request.getUserId()))
                ).orElseThrow(() -> new ForumNotFoundException(request.getForumId()));
    }

    @Override
    public Post updatePost(PostUpdateRequest request) {
        TSID postId = TSID.from(request.getPostId());
        return postRepository.findByIdAndStatus(postId.toLong(), PostStatus.ACTIVE)
                .map(post -> {
                    Optional.ofNullable(request.getTitle())
                            .filter(title -> !title.isEmpty() && !Objects.equals(post.getTitle(), title))
                            .ifPresent(post::setTitle);
                    Optional.ofNullable(request.getContent())
                            .filter(content -> !content.isEmpty() && !Objects.equals(post.getContent(), content))
                            .ifPresent(post::setContent);
                    Optional.ofNullable(request.getForumId())
                            .filter(frId -> !frId.isEmpty() && !Objects.equals(post.getForum().getId(), TSID.from(frId).toLong()))
                            .map(TSID::from)
                            .map(TSID::toLong)
                            .ifPresent(frId -> forumRepository.findByIdAndStatus(frId, ForumStatus.ACTIVE)
                                    .ifPresentOrElse(post::setForum, () -> {
                                        throw new ForumNotFoundException(request.getForumId());
                                    }));
                    Optional.ofNullable(request.getUserId())
                            .filter(userId -> !userId.isEmpty() && !Objects.equals(post.getUser().getId(), TSID.from(userId).toLong()))
                            .map(TSID::from)
                            .map(TSID::toLong)
                            .ifPresent(userId -> userRepository.findByIdAndActiveTrue(userId)
                                    .ifPresentOrElse(post::setUser, () -> {
                                        throw new UserNotFoundException(request.getUserId());
                                    }));
                    return postRepository.save(post);
                })
                .orElseThrow(() -> new PostNotFoundException(request.getForumId()));
    }

    @Override
    public void deactivatePost(String postId) {
        postRepository.findById(TSID.from(postId).toLong())
                .ifPresentOrElse(post -> {
                    if (PostStatus.INACTIVE.equals(post.getStatus())) {
                        return;
                    }
                    post.setStatus(PostStatus.INACTIVE);
                    postRepository.save(post);
                }, () -> {
                    throw new PostNotFoundException(postId);
                });
    }
}
