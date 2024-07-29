package com.boolfly.grademanagementrestful.service;

import com.boolfly.grademanagementrestful.api.dto.user.SearchUserRequest;
import com.boolfly.grademanagementrestful.api.dto.user.StudentRegistrationRequest;
import com.boolfly.grademanagementrestful.api.dto.user.StudentUpdateRequest;
import com.boolfly.grademanagementrestful.domain.User;
import com.boolfly.grademanagementrestful.service.base.UserService;
import org.springframework.data.domain.Page;

public interface StudentService extends UserService {
    User createStudent(StudentRegistrationRequest request);

    Page<User> getStudents(int page, int size, SearchUserRequest request);

    User updateStudent(StudentUpdateRequest request);

    void deactivateStudent(String studentId);
}
