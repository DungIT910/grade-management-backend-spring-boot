package com.boolfly.grademanagementrestful.api.impl;

import com.boolfly.grademanagementrestful.api.StudentResource;
import com.boolfly.grademanagementrestful.api.dto.general.PageResponse;
import com.boolfly.grademanagementrestful.api.dto.user.SearchUserRequest;
import com.boolfly.grademanagementrestful.api.dto.user.StudentRegistrationRequest;
import com.boolfly.grademanagementrestful.api.dto.user.StudentResponse;
import com.boolfly.grademanagementrestful.api.dto.user.StudentUpdateRequest;
import com.boolfly.grademanagementrestful.domain.User;
import com.boolfly.grademanagementrestful.domain.model.role.RoleModel;
import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;
import com.boolfly.grademanagementrestful.listener.event.UploadAvatarEvent;
import com.boolfly.grademanagementrestful.mapper.UserMapper;
import com.boolfly.grademanagementrestful.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentResourceImpl implements StudentResource {
    private static final UserMapper userMapper = UserMapper.INSTANCE;
    private final StudentService studentService;
    private final ApplicationEventPublisher eventPublisher;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Override
    public StudentResponse createStudent(StudentRegistrationRequest request, MultipartFile avatar) {
        try {
            User createdStudent = studentService.createStudent(request);
            eventPublisher.publishEvent(UploadAvatarEvent.builder()
                    .source(this)
                    .user(createdStudent)
                    .avatar(avatar.getBytes())
                    .build());
            return userMapper.toStudentResponse(createdStudent);
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }

    @PostMapping("/search")
    @Override
    public PageResponse<StudentResponse> getStudents(int page, int size, SearchUserRequest request) {
        try {
            request = request.withRoles(List.of(RoleModel.ROLE_STUDENT));
            Page<User> pageUser = studentService.getStudents(page, size, request);
            Page<StudentResponse> pStudentResponse = pageUser
                    .map(userMapper::toStudentResponse);
            PageResponse<StudentResponse> pageResponse = new PageResponse<>();
            pageResponse.setContent(pStudentResponse.getContent());
            return pageResponse;
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }

    @PutMapping
    @Override
    public StudentResponse updateStudent(StudentUpdateRequest request) {
        try {
            User u = studentService.updateStudent(request);
            return userMapper.toStudentResponse(u);
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }

    @DeleteMapping("/{studentId}")
    @Override
    public void deactivateStudent(@PathVariable String studentId) {
        try {
            studentService.deactivateStudent(studentId);
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }
}
