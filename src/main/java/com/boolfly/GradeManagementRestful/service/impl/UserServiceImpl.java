package com.boolfly.GradeManagementRestful.service.impl;

import com.boolfly.GradeManagementRestful.api.dto.user.SearchUserRequest;
import com.boolfly.GradeManagementRestful.api.dto.user.StudentRegistrationRequest;
import com.boolfly.GradeManagementRestful.api.dto.user.StudentUpdateRequest;
import com.boolfly.GradeManagementRestful.domain.QUser;
import com.boolfly.GradeManagementRestful.domain.Role;
import com.boolfly.GradeManagementRestful.domain.User;
import com.boolfly.GradeManagementRestful.domain.model.role.RoleModel;
import com.boolfly.GradeManagementRestful.exception.role.RoleNotFoundException;
import com.boolfly.GradeManagementRestful.exception.user.EmailTakenException;
import com.boolfly.GradeManagementRestful.exception.user.IsNotStudentException;
import com.boolfly.GradeManagementRestful.exception.user.StudentEmailNotFoundException;
import com.boolfly.GradeManagementRestful.exception.user.StudentNotFoundException;
import com.boolfly.GradeManagementRestful.repository.RoleRepository;
import com.boolfly.GradeManagementRestful.repository.UserRepository;
import com.boolfly.GradeManagementRestful.service.UserService;
import com.querydsl.core.types.dsl.BooleanExpression;
import io.hypersistence.tsid.TSID;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
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
        List<RoleModel> roles = request.getRoles()
                .stream()
                .filter(RoleModel.ROLE_STUDENT::equals)
                .toList();
        QUser queryUser = QUser.user;
        BooleanExpression predicate = queryUser.role.name.in(roles
                .stream()
                .map(RoleModel::getRoleName)
                .toList());

        return userRepository.findAll(predicate, PageRequest.of(page, size));
    }

    @Override
    @Transactional
    public User updateStudent(StudentUpdateRequest request) {
        Optional<Role> role = roleRepository.findByName(RoleModel.ROLE_STUDENT.getRoleName());
        if (role.isEmpty()) {
            throw new RoleNotFoundException(RoleModel.ROLE_STUDENT.getRoleType());
        }
        userRepository.findById(TSID.from(request.getStudentId()).toLong()).map(user1 -> {
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
                    .ifPresent(e -> {
                        userRepository.findByEmail(e)
                                .ifPresentOrElse(st -> {
                                    throw new EmailTakenException(e);
                                }, () -> user1.setEmail(e));
                    });
            return user1;
        }).orElseThrow(StudentNotFoundException::new);
        return userRepository.findById(TSID.from(request.getStudentId()).toLong())
                .orElseThrow(StudentNotFoundException::new);
    }

    @Override
    public void deactivateStudent(String studentId) {
        userRepository.findById(TSID.from(studentId).toLong())
                .map(user -> {
                    if (user.getActive()) {
                        user.setActive(false);
                        return userRepository.save(user);
                    }
                    return user;
                })
                .orElseThrow(StudentNotFoundException::new);
    }


}
