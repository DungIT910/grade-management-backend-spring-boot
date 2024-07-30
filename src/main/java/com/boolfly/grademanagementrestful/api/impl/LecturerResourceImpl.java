package com.boolfly.grademanagementrestful.api.impl;

import com.boolfly.grademanagementrestful.api.LecturerResource;
import com.boolfly.grademanagementrestful.api.dto.general.PageResponse;
import com.boolfly.grademanagementrestful.api.dto.user.LecturerAddRequest;
import com.boolfly.grademanagementrestful.api.dto.user.LecturerResponse;
import com.boolfly.grademanagementrestful.api.dto.user.LecturerUpdateRequest;
import com.boolfly.grademanagementrestful.api.dto.user.SearchUserRequest;
import com.boolfly.grademanagementrestful.domain.User;
import com.boolfly.grademanagementrestful.domain.model.role.RoleModel;
import com.boolfly.grademanagementrestful.mapper.UserMapper;
import com.boolfly.grademanagementrestful.service.LecturerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lecturers")
@RequiredArgsConstructor
public class LecturerResourceImpl implements LecturerResource {
    private static final UserMapper userMapper = UserMapper.INSTANCE;
    private final LecturerService lecturerService;

    @PostMapping
    @Override
    public LecturerResponse addLecturer(LecturerAddRequest request) {
        User newLecturer = lecturerService.addLecturer(request);
        return userMapper.toLecturerResponse(newLecturer);
    }

    @PostMapping("/search")
    @Override
    public PageResponse<LecturerResponse> getLecturers(int page, int size, SearchUserRequest request) {
        request = request.withRoles(List.of(RoleModel.ROLE_LECTURER));
        Page<User> pageUser = lecturerService.getLecturers(page, size, request);
        Page<LecturerResponse> pageLecResponse = pageUser
                .map(userMapper::toLecturerResponse);
        PageResponse<LecturerResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(pageLecResponse.getContent());
        return pageResponse;
    }

    @PutMapping
    @Override
    public LecturerResponse updateLecturer(LecturerUpdateRequest request) {
        return userMapper.toLecturerResponse(lecturerService.updateLecturer(request));
    }

    @DeleteMapping("/{lecturerId}")
    @Override
    public void deactivateLecturer(@PathVariable String lecturerId) {
        lecturerService.deactivateLecturer(lecturerId);
    }
}

