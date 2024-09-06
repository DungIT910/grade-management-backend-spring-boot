package com.boolfly.grademanagementrestful.service.impl;

import com.boolfly.grademanagementrestful.api.dto.forum.ForumAddRequest;
import com.boolfly.grademanagementrestful.api.dto.forum.ForumUpdateRequest;
import com.boolfly.grademanagementrestful.api.dto.forum.SearchForumRequest;
import com.boolfly.grademanagementrestful.builder.base.SearchParamsBuilder;
import com.boolfly.grademanagementrestful.builder.forum.ForumSearchParamsBuilder;
import com.boolfly.grademanagementrestful.domain.Forum;
import com.boolfly.grademanagementrestful.domain.model.course.CourseStatus;
import com.boolfly.grademanagementrestful.domain.model.forum.ForumStatus;
import com.boolfly.grademanagementrestful.domain.model.post.PostStatus;
import com.boolfly.grademanagementrestful.exception.course.CourseNotFoundException;
import com.boolfly.grademanagementrestful.exception.forum.ForumNotFoundException;
import com.boolfly.grademanagementrestful.repository.CourseRepository;
import com.boolfly.grademanagementrestful.repository.ForumRepository;
import com.boolfly.grademanagementrestful.repository.PostRepository;
import com.boolfly.grademanagementrestful.service.ForumService;
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
public class ForumServiceImpl implements ForumService {
    private final ForumRepository forumRepository;
    private final CourseRepository courseRepository;
    private final PostRepository postRepository;

    @Override
    public Page<Forum> getForums(int page, int size, SearchForumRequest request) {
        SearchParamsBuilder builder = ForumSearchParamsBuilder.from(page, size, request);
        Optional<BooleanExpression> expression = builder.getCommonCriteria();
        Pageable pageable = builder.getPageable();
        return expression.map(ex -> forumRepository.findAll(ex, pageable))
                .orElseGet(() -> forumRepository.findAll(pageable));
    }

    @Override
    public Forum addForum(ForumAddRequest request) {
        TSID courseId = TSID.from(request.getCourseId());
        return courseRepository.findByIdAndStatusNot(courseId.toLong(), CourseStatus.INACTIVE)
                .map(c -> forumRepository.save(
                        Forum.builder()
                                .id(TSID.fast().toLong())
                                .name(request.getName())
                                .description(request.getDescription())
                                .course(c)
                                .status(ForumStatus.ACTIVE)
                                .build())
                ).orElseThrow(() -> new CourseNotFoundException(request.getCourseId()));
    }

    @Override
    public Forum updateForum(ForumUpdateRequest request) {
        String forumIdAsString = request.getForumId();
        TSID forumId = TSID.from(forumIdAsString);
        return forumRepository.findByIdAndStatus(forumId.toLong(), ForumStatus.ACTIVE)
                .map(forum -> {
                    Optional.ofNullable(request.getName())
                            .filter(name -> !name.isEmpty() && !Objects.equals(forum.getName(), name))
                            .ifPresent(forum::setName);

                    Optional.ofNullable(request.getDescription())
                            .filter(des -> !des.isEmpty() && !Objects.equals(forum.getDescription(), des))
                            .ifPresent(forum::setDescription);

                    Optional.ofNullable(request.getCourseId())
                            .filter(courseId -> !courseId.isEmpty())
                            .map(TSID::from)
                            .filter(courseId -> !Objects.equals(forum.getCourse().getId(), courseId.toLong()))
                            .map(courseId -> courseRepository.findByIdAndStatusNot(courseId.toLong(), CourseStatus.INACTIVE)
                                    .orElseThrow(() -> new CourseNotFoundException(courseId.toString())))
                            .ifPresent(forum::setCourse);
                    return forum;
                })
                .map(forumRepository::save)
                .orElseThrow(() -> new ForumNotFoundException(forumIdAsString));
    }

    @Override
    public void deactivateForum(String forumId) {
        Long forumIdAsLong = TSID.from(forumId).toLong();
        Forum forum = forumRepository.findByIdAndStatus(forumIdAsLong, ForumStatus.ACTIVE)
                .orElseThrow(() -> new ForumNotFoundException(forumId));

        forum.setStatus(ForumStatus.INACTIVE);
        forumRepository.save(forum);

        postRepository.findAllByForum_Id(forumIdAsLong)
                .forEach(post -> {
                    post.setStatus(PostStatus.INACTIVE);
                    postRepository.save(post);
                });
    }
}
