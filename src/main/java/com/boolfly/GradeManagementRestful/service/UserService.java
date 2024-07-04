package com.boolfly.GradeManagementRestful.service;

import com.boolfly.GradeManagementRestful.api.dto.user.StudentRegistrationRequest;
import com.boolfly.GradeManagementRestful.domain.User;
import org.springframework.data.domain.Page;

public interface UserService {
    User createStudent(StudentRegistrationRequest request);

    Page<User> getStudents(int page, int size);
}
