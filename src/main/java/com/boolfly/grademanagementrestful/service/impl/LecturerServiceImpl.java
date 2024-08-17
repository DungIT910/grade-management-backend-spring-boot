package com.boolfly.grademanagementrestful.service.impl;

import com.boolfly.grademanagementrestful.api.dto.user.LecturerAddRequest;
import com.boolfly.grademanagementrestful.api.dto.user.LecturerUpdateRequest;
import com.boolfly.grademanagementrestful.api.dto.user.SearchUserRequest;
import com.boolfly.grademanagementrestful.builder.user.UserSearchParamsBuilder;
import com.boolfly.grademanagementrestful.domain.User;
import com.boolfly.grademanagementrestful.domain.model.role.RoleModel;
import com.boolfly.grademanagementrestful.repository.RoleRepository;
import com.boolfly.grademanagementrestful.repository.UserRepository;
import com.boolfly.grademanagementrestful.service.LecturerService;
import com.boolfly.grademanagementrestful.service.base.UserServiceImpl;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LecturerServiceImpl extends UserServiceImpl implements LecturerService {

    @Override
    public Page<User> getLecturers(int page, int size, SearchUserRequest request) {
        UserSearchParamsBuilder searchParamsBuilder = UserSearchParamsBuilder.from(page, size, request);
        Optional<BooleanExpression> predicate = searchParamsBuilder.getCommonCriteria();
        Pageable pageable = searchParamsBuilder.getPageable();

        return predicate.map(p -> userRepository.findAll(p, pageable))
                .orElseGet(() -> userRepository.findAll(pageable));
    }

    @Override
    public User addLecturer(LecturerAddRequest request) {
        return create(request, RoleModel.ROLE_LECTURER);
    }

    @Override
    public User updateLecturer(LecturerUpdateRequest request) {
        return update(request, RoleModel.ROLE_LECTURER);
    }

    @Override
    public void deactivateLecturer(String lecturerId) {
        deactivate(lecturerId, RoleModel.ROLE_LECTURER);
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
}
