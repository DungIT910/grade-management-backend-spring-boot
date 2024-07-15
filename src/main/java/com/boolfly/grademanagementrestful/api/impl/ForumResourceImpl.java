package com.boolfly.grademanagementrestful.api.impl;

import com.boolfly.grademanagementrestful.api.ForumResource;
import com.boolfly.grademanagementrestful.api.dto.forum.ForumAddRequest;
import com.boolfly.grademanagementrestful.api.dto.forum.ForumResponse;
import com.boolfly.grademanagementrestful.api.dto.forum.ForumUpdateRequest;
import com.boolfly.grademanagementrestful.api.dto.forum.SearchForumRequest;
import com.boolfly.grademanagementrestful.api.dto.general.PageResponse;
import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;
import com.boolfly.grademanagementrestful.mapper.ForumMapper;
import com.boolfly.grademanagementrestful.service.ForumService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/forums")
@RequiredArgsConstructor
public class ForumResourceImpl implements ForumResource {
    private static final ForumMapper forumMapper = ForumMapper.INSTANCE;
    private final ForumService forumService;

    @PostMapping("/search")
    @Override
    public PageResponse<ForumResponse> getForums(int page, int size, SearchForumRequest request) {
        try {
            Page<ForumResponse> pageForum = forumService.getForums(page, size, request).map(forumMapper::toForumResponse);
            PageResponse<ForumResponse> pageForumResponse = new PageResponse<>();
            pageForumResponse.setContent(pageForum.getContent());
            return pageForumResponse;
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }

    @PostMapping
    @Override
    public ForumResponse addForum(ForumAddRequest request) {
        try {
            return forumMapper.toForumResponse(forumService.addForum(request));
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }

    @PutMapping
    @Override
    public ForumResponse updateForum(ForumUpdateRequest request) {
        try {
            return forumMapper.toForumResponse(forumService.updateForum(request));
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }

    @DeleteMapping("/{forumId}")
    @Override
    public void deactivateForum(@PathVariable String forumId) {
        try {
            forumService.deactivateForum(forumId);
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }
}
