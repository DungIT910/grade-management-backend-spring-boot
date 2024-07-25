package com.boolfly.grademanagementrestful.service.impl;

import com.boolfly.grademanagementrestful.api.dto.user.LecturerAddRequest;
import com.boolfly.grademanagementrestful.api.dto.user.LecturerUpdateRequest;
import com.boolfly.grademanagementrestful.api.dto.user.SearchUserRequest;
import com.boolfly.grademanagementrestful.builder.user.UserSearchParamsBuilder;
import com.boolfly.grademanagementrestful.domain.Role;
import com.boolfly.grademanagementrestful.domain.User;
import com.boolfly.grademanagementrestful.domain.model.role.RoleModel;
import com.boolfly.grademanagementrestful.exception.role.RoleNotFoundException;
import com.boolfly.grademanagementrestful.exception.user.EmailNotFoundException;
import com.boolfly.grademanagementrestful.exception.user.EmailTakenException;
import com.boolfly.grademanagementrestful.exception.user.IsNotLecturerException;
import com.boolfly.grademanagementrestful.exception.user.LecturerNotFoundException;
import com.boolfly.grademanagementrestful.repository.RoleRepository;
import com.boolfly.grademanagementrestful.repository.UserRepository;
import com.boolfly.grademanagementrestful.service.LecturerService;
import com.querydsl.core.types.dsl.BooleanExpression;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LecturerServiceImpl implements LecturerService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

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
        String email = request.getEmail();
        userRepository.findByEmail(email)
                .ifPresentOrElse(user -> {
                    throw new EmailTakenException(user.getEmail());
                }, () -> roleRepository.findByName(RoleModel.ROLE_LECTURER.getRoleName())
                        .map(role -> userRepository.save(User.builder()
                                .id(TSID.fast().toLong())
                                .email(email)
                                .firstName(request.getFirstName())
                                .lastName(request.getLastName())
                                .password(request.getPassword())
                                .active(true)
                                .role(role)
                                .build()))
                        .orElseThrow(() -> new RoleNotFoundException(RoleModel.ROLE_LECTURER.getRoleType())));
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException(email));
    }

    @Override
    public User updateLecturer(LecturerUpdateRequest request) {
        Optional<Role> role = roleRepository.findByName(RoleModel.ROLE_LECTURER.getRoleName());
        if (role.isEmpty()) {
            throw new RoleNotFoundException(RoleModel.ROLE_LECTURER.getRoleType());
        }
        return userRepository.findByIdAndActiveTrue(TSID.from(request.getLecturerId()).toLong()).map(user -> {
                    if (!user.getRole().getId().equals(role.get().getId())) {
                        throw new IsNotLecturerException();
                    }
                    Optional.ofNullable(request.getFirstName())
                            .filter(fn -> !fn.isEmpty() && !Objects.equals(user.getFirstName(), fn))
                            .ifPresent(user::setFirstName);
                    Optional.ofNullable(request.getLastName())
                            .filter(ln -> !ln.isEmpty() && !Objects.equals(user.getLastName(), ln))
                            .ifPresent(user::setLastName);
                    Optional.ofNullable(request.getEmail())
                            .filter(e -> !e.isEmpty() && !Objects.equals(user.getEmail(), e))
                            .ifPresent(e -> userRepository.findByEmail(e)
                                    .ifPresentOrElse(st -> {
                                        throw new EmailTakenException(e);
                                    }, () -> user.setEmail(e)));
                    return user;
                })
                .orElseThrow(LecturerNotFoundException::new);
    }

    @Override
    public void deactivateLecturer(String lecturerId) {
        userRepository.findById(TSID.from(lecturerId).toLong())
                .filter(user -> RoleModel.ROLE_LECTURER.getRoleName()
                        .equals(user.getRole().getName()))
                .ifPresentOrElse(user -> {
                    if (Boolean.TRUE.equals(user.getActive())) {
                        user.setActive(false);
                        userRepository.save(user);
                    }
                }, () -> {
                    throw new LecturerNotFoundException();
                });
    }
}
