package com.boolfly.grademanagementrestful.api.impl;

import com.boolfly.grademanagementrestful.api.LecturerResource;
import com.boolfly.grademanagementrestful.api.dto.general.PageResponse;
import com.boolfly.grademanagementrestful.api.dto.user.LecturerAddRequest;
import com.boolfly.grademanagementrestful.api.dto.user.LecturerResponse;
import com.boolfly.grademanagementrestful.api.dto.user.LecturerUpdateRequest;
import com.boolfly.grademanagementrestful.api.dto.user.SearchUserRequest;
import com.boolfly.grademanagementrestful.domain.User;
import com.boolfly.grademanagementrestful.domain.model.role.RoleModel;
import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;
import com.boolfly.grademanagementrestful.listener.event.UploadAvatarEvent;
import com.boolfly.grademanagementrestful.mapper.UserMapper;
import com.boolfly.grademanagementrestful.service.LecturerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lecturers")
@RequiredArgsConstructor
public class LecturerResourceImpl implements LecturerResource {
    private static final UserMapper userMapper = UserMapper.INSTANCE;
    private final LecturerService lecturerService;
    private final ApplicationEventPublisher eventPublisher;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Override
    public LecturerResponse addLecturer(LecturerAddRequest request, MultipartFile avatar) {
        try {
            User newLecturer = lecturerService.addLecturer(request);
            eventPublisher.publishEvent(UploadAvatarEvent.builder()
                    .source(this)
                    .user(newLecturer)
                    .avatar(avatar.getBytes())
                    .build());
            return userMapper.toLecturerResponse(newLecturer);
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }

    @PostMapping("/search")
    @Override
    public PageResponse<LecturerResponse> getLecturers(int page, int size, SearchUserRequest request) {
        try {
            request = request.withRoles(List.of(RoleModel.ROLE_LECTURER));
            Page<User> pageUser = lecturerService.getLecturers(page, size, request);
            Page<LecturerResponse> pageLecResponse = pageUser
                    .map(userMapper::toLecturerResponse);
            PageResponse<LecturerResponse> pageResponse = new PageResponse<>();
            pageResponse.setContent(pageLecResponse.getContent());
            return pageResponse;
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }

    @PutMapping
    @Override
    public LecturerResponse updateLecturer(LecturerUpdateRequest request) {
        try {
            return userMapper.toLecturerResponse(lecturerService.updateLecturer(request));
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }

    @DeleteMapping("/{lecturerId}")
    @Override
    public void deactivateLecturer(@PathVariable String lecturerId) {
        try {
            lecturerService.deactivateLecturer(lecturerId);
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }

    @GetMapping("/{lecturerId}")
    @Override
    public LecturerResponse getLecturer(@PathVariable String lecturerId) {
        return userMapper.toLecturerResponse(lecturerService.getUser(lecturerId));
    }
}
