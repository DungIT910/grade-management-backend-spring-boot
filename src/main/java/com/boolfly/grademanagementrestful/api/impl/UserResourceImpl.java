package com.boolfly.grademanagementrestful.api.impl;

import com.boolfly.grademanagementrestful.api.UserResource;
import com.boolfly.grademanagementrestful.api.dto.general.PageResponse;
import com.boolfly.grademanagementrestful.api.dto.user.SearchUserRequest;
import com.boolfly.grademanagementrestful.api.dto.user.StudentRegistrationRequest;
import com.boolfly.grademanagementrestful.api.dto.user.StudentResponse;
import com.boolfly.grademanagementrestful.api.dto.user.StudentUpdateRequest;
import com.boolfly.grademanagementrestful.domain.User;
import com.boolfly.grademanagementrestful.domain.model.role.RoleModel;
import com.boolfly.grademanagementrestful.mapper.UserMapper;
import com.boolfly.grademanagementrestful.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserResourceImpl implements UserResource {
    private static final UserMapper userMapper = UserMapper.INSTANCE;
    private final UserService userService;

    @PostMapping("/student")
    @Override
    public StudentResponse createStudent(StudentRegistrationRequest request) {
        User createdStudent = userService.createStudent(request);
        return userMapper.toStudentResponse(createdStudent);
    }

    @PostMapping("/students")
    @Override
    public PageResponse<StudentResponse> getStudents(int page, int size, SearchUserRequest request) {
        request = request.withRoles(List.of(RoleModel.ROLE_STUDENT));
        Page<User> pageUser = userService.getStudents(page, size, request);
        Page<StudentResponse> pStudentResponse = pageUser
                .map(userMapper::toStudentResponse);
        PageResponse<StudentResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(pStudentResponse.getContent());
        return pageResponse;
    }

    @PutMapping("/student")
    @Override
    public StudentResponse updateStudent(StudentUpdateRequest request) {
        User u = userService.updateStudent(request);
        return userMapper.toStudentResponse(u);
    }

    @DeleteMapping("/students/{studentId}")
    @Override
    public void deactivateStudent(@PathVariable String studentId) {
        userService.deactivateStudent(studentId);
    }

}
