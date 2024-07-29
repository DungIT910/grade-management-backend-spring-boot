package com.boolfly.grademanagementrestful.service.impl;

import com.boolfly.grademanagementrestful.api.dto.user.SearchUserRequest;
import com.boolfly.grademanagementrestful.api.dto.user.StudentRegistrationRequest;
import com.boolfly.grademanagementrestful.api.dto.user.StudentUpdateRequest;
import com.boolfly.grademanagementrestful.builder.user.UserSearchParamsBuilder;
import com.boolfly.grademanagementrestful.domain.User;
import com.boolfly.grademanagementrestful.domain.model.role.RoleModel;
import com.boolfly.grademanagementrestful.repository.RoleRepository;
import com.boolfly.grademanagementrestful.repository.UserRepository;
import com.boolfly.grademanagementrestful.service.StudentService;
import com.boolfly.grademanagementrestful.service.base.UserServiceImpl;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl extends UserServiceImpl implements StudentService {

    @Override
    public User createStudent(StudentRegistrationRequest request) {
        return create(request, RoleModel.ROLE_STUDENT);
    }

    @Override
    public Page<User> getStudents(int page, int size, SearchUserRequest request) {
        UserSearchParamsBuilder searchParamsBuilder = UserSearchParamsBuilder.from(page, size, request);
        BooleanExpression predicate = searchParamsBuilder.getCommonCriteriaValue();
        Pageable pageable = searchParamsBuilder.getPageable();

        return userRepository.findAll(predicate, pageable);
    }

    @Override
    @Transactional
    public User updateStudent(StudentUpdateRequest request) {
        return update(request, RoleModel.ROLE_STUDENT);
    }

    @Override
    public void deactivateStudent(String studentId) {
        deactivate(studentId, RoleModel.ROLE_STUDENT);
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
