package com.boolfly.GradeManagementRestful.api.impl;

import com.boolfly.GradeManagementRestful.api.UserResource;
import com.boolfly.GradeManagementRestful.api.dto.general.PageResponse;
import com.boolfly.GradeManagementRestful.api.dto.user.StudentRegistrationRequest;
import com.boolfly.GradeManagementRestful.api.dto.user.StudentResponse;
import com.boolfly.GradeManagementRestful.domain.User;
import com.boolfly.GradeManagementRestful.mapper.UserMapper;
import com.boolfly.GradeManagementRestful.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/students")
    @Override
    public PageResponse<StudentResponse> getStudents(int page, int size) {
        Page<User> pageUser = userService.getStudents(page, size);
        Page<StudentResponse> pStudentResponse = pageUser
                .map(userMapper::toStudentResponse);
        PageResponse<StudentResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(pStudentResponse.getContent());
        return pageResponse;
    }
}
