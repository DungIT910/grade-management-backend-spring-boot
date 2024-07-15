package com.boolfly.grademanagementrestful.service.impl;

import com.boolfly.grademanagementrestful.api.dto.forum.ForumAddRequest;
import com.boolfly.grademanagementrestful.api.dto.forum.ForumUpdateRequest;
import com.boolfly.grademanagementrestful.api.dto.forum.SearchForumRequest;
import com.boolfly.grademanagementrestful.builder.base.SearchParamsBuilder;
import com.boolfly.grademanagementrestful.builder.forum.ForumSearchParamsBuilder;
import com.boolfly.grademanagementrestful.domain.Forum;
import com.boolfly.grademanagementrestful.domain.model.forum.ForumStatus;
import com.boolfly.grademanagementrestful.exception.course.CourseNotFoundException;
import com.boolfly.grademanagementrestful.exception.forum.ForumNotFoundException;
import com.boolfly.grademanagementrestful.repository.CourseRepository;
import com.boolfly.grademanagementrestful.repository.ForumRepository;
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
        return courseRepository.findById(courseId.toLong())
                .map(c ->
                        forumRepository.save(
                                Forum.builder()
                                        .id(TSID.fast().toLong())
                                        .name(request.getName())
                                        .description(request.getDescription())
                                        .course(c)
                                        .status(ForumStatus.ACTIVE)
                                        .build()
                        )
                ).orElseThrow(() -> new CourseNotFoundException(request.getCourseId()));
    }

    @Override
    public Forum updateForum(ForumUpdateRequest request) {
        TSID forumId = TSID.from(request.getForumId());
        return forumRepository.findById(forumId.toLong())
                .map(forum -> {
                    Optional.ofNullable(request.getName())
                            .filter(name -> !name.isEmpty() && !Objects.equals(forum.getName(), name))
                            .ifPresent(forum::setName);
                    Optional.ofNullable(request.getDescription())
                            .filter(des -> !des.isEmpty() && !Objects.equals(forum.getDescription(), des))
                            .ifPresent(forum::setDescription);
                    Optional.ofNullable(request.getCourseId())
                            .filter(courseId -> !courseId.isEmpty() && !Objects.equals(forum.getCourse().getId(), TSID.from(courseId).toLong()))
                            .map(TSID::from)
                            .map(TSID::toLong)
                            .ifPresent(courseId -> courseRepository.findById(courseId)
                                    .ifPresentOrElse(forum::setCourse, () -> {
                                        throw new CourseNotFoundException(request.getCourseId());
                                    }));
                    return forumRepository.save(forum);
                }).orElseThrow(() -> new ForumNotFoundException(request.getForumId()));
    }

    @Override
    public void deactivateForum(String forumId) {
        forumRepository.findById(TSID.from(forumId).toLong())
                .ifPresentOrElse(forum -> {
                    if (ForumStatus.INACTIVE.equals(forum.getStatus())) {
                        return;
                    }
                    forum.setStatus(ForumStatus.INACTIVE);
                    forumRepository.save(forum);
                }, () -> {
                    throw new ForumNotFoundException(forumId);
                });
    }
}
