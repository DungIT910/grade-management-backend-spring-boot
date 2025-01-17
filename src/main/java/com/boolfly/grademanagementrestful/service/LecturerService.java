package com.boolfly.grademanagementrestful.service;

import com.boolfly.grademanagementrestful.api.dto.user.LecturerAddRequest;
import com.boolfly.grademanagementrestful.api.dto.user.LecturerUpdateRequest;
import com.boolfly.grademanagementrestful.api.dto.user.SearchUserRequest;
import com.boolfly.grademanagementrestful.domain.User;
import com.boolfly.grademanagementrestful.service.base.UserService;
import org.springframework.data.domain.Page;

public interface LecturerService extends UserService {
    Page<User> getLecturers(int page, int size, SearchUserRequest request);

    User addLecturer(LecturerAddRequest request);

    User updateLecturer(LecturerUpdateRequest request);

    void deactivateLecturer(String lecturerId);
}
