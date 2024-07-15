package com.boolfly.grademanagementrestful.service.impl;

import com.boolfly.grademanagementrestful.api.dto.user.SearchUserRequest;
import com.boolfly.grademanagementrestful.api.dto.user.StudentRegistrationRequest;
import com.boolfly.grademanagementrestful.api.dto.user.StudentUpdateRequest;
import com.boolfly.grademanagementrestful.builder.user.StudentSearchParamsBuilder;
import com.boolfly.grademanagementrestful.domain.Role;
import com.boolfly.grademanagementrestful.domain.User;
import com.boolfly.grademanagementrestful.domain.model.role.RoleModel;
import com.boolfly.grademanagementrestful.exception.role.RoleNotFoundException;
import com.boolfly.grademanagementrestful.exception.user.EmailTakenException;
import com.boolfly.grademanagementrestful.exception.user.IsNotStudentException;
import com.boolfly.grademanagementrestful.exception.user.StudentEmailNotFoundException;
import com.boolfly.grademanagementrestful.exception.user.StudentNotFoundException;
import com.boolfly.grademanagementrestful.repository.RoleRepository;
import com.boolfly.grademanagementrestful.repository.UserRepository;
import com.boolfly.grademanagementrestful.service.UserService;
import com.querydsl.core.types.dsl.BooleanExpression;
import io.hypersistence.tsid.TSID;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    @Override
    public User createStudent(StudentRegistrationRequest request) {
        String email = request.getEmail();
        userRepository.findByEmail(email)
                .ifPresentOrElse(user -> {
                    throw new EmailTakenException(user.getEmail());
                }, () -> roleRepository.findByName(RoleModel.ROLE_STUDENT.getRoleName())
                        .map(role -> userRepository.save(User.builder()
                                .id(TSID.fast().toLong())
                                .email(email)
                                .firstName(request.getFirstName())
                                .lastName(request.getLastName())
                                .password(request.getPassword())
                                .role(role)
                                .build()))
                        .orElseThrow(() -> new RoleNotFoundException(RoleModel.ROLE_STUDENT.getRoleType())));
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new StudentEmailNotFoundException(email));
    }

    @Override
    public Page<User> getStudents(int page, int size, SearchUserRequest request) {
        StudentSearchParamsBuilder searchParamsBuilder = StudentSearchParamsBuilder.from(page, size, request);
        BooleanExpression predicate = searchParamsBuilder.getCommonCriteriaValue();
        Pageable pageable = searchParamsBuilder.getPageable();

        return userRepository.findAll(predicate, pageable);
    }

    @Override
    @Transactional
    public User updateStudent(StudentUpdateRequest request) {
        Optional<Role> role = roleRepository.findByName(RoleModel.ROLE_STUDENT.getRoleName());
        if (role.isEmpty()) {
            throw new RoleNotFoundException(RoleModel.ROLE_STUDENT.getRoleType());
        }
        return userRepository.findById(TSID.from(request.getStudentId()).toLong()).map(user1 -> {
                    if (!user1.getRole().getId().equals(role.get().getId())) {
                        throw new IsNotStudentException();
                    }
                    Optional.ofNullable(request.getFirstName())
                            .filter(fn -> !fn.isEmpty() && !Objects.equals(user1.getFirstName(), fn))
                            .ifPresent(user1::setFirstName);
                    Optional.ofNullable(request.getLastName())
                            .filter(ln -> !ln.isEmpty() && !Objects.equals(user1.getLastName(), ln))
                            .ifPresent(user1::setLastName);
                    Optional.ofNullable(request.getEmail())
                            .filter(e -> !e.isEmpty() && !Objects.equals(user1.getEmail(), e))
                            .ifPresent(e -> userRepository.findByEmail(e)
                                    .ifPresentOrElse(st -> {
                                        throw new EmailTakenException(e);
                                    }, () -> user1.setEmail(e)));
                    return user1;
                })
                .orElseThrow(StudentNotFoundException::new);
    }

    @Override
    public void deactivateStudent(String studentId) {
        userRepository.findById(TSID.from(studentId).toLong())
                .ifPresentOrElse(user -> {
                    if (Boolean.TRUE.equals(user.getActive())) {
                        user.setActive(false);
                        userRepository.save(user);
                    }
                }, () -> {
                    throw new StudentNotFoundException();
                });
    }


}
