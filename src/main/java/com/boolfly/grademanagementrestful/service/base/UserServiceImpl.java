package com.boolfly.grademanagementrestful.service.base;

import com.boolfly.grademanagementrestful.api.dto.user.UserRegistrationRequest;
import com.boolfly.grademanagementrestful.api.dto.user.UserUpdateRequest;
import com.boolfly.grademanagementrestful.domain.Role;
import com.boolfly.grademanagementrestful.domain.User;
import com.boolfly.grademanagementrestful.domain.model.role.RoleModel;
import com.boolfly.grademanagementrestful.exception.role.RoleNotFoundException;
import com.boolfly.grademanagementrestful.exception.user.EmailTakenException;
import com.boolfly.grademanagementrestful.exception.user.LecturerNotFoundException;
import com.boolfly.grademanagementrestful.exception.user.StudentNotFoundException;
import com.boolfly.grademanagementrestful.repository.RoleRepository;
import com.boolfly.grademanagementrestful.repository.UserRepository;
import io.hypersistence.tsid.TSID;

public abstract class UserServiceImpl implements UserService {
    protected UserRepository userRepository;
    protected RoleRepository roleRepository;

    @Override
    public User create(UserRegistrationRequest request, RoleModel roleModel) {
        String email = request.getEmail();
        String roleName = roleModel.getRoleName();

        if (userRepository.existsByEmail(email)) {
            throw new EmailTakenException(email);
        }

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(roleModel.getRoleType()));

        return userRepository.save(
                User.builder()
                        .id(TSID.fast().toLong())
                        .email(email)
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .password(request.getPassword())
                        .active(true)
                        .role(role)
                        .build()
        );
    }

    @Override
    public void deactivate(String id, RoleModel roleModel) {
        User user = userRepository.findByIdAndRole_Name(TSID.from(id).toLong(), roleModel.getRoleName())
                .orElseThrow(() -> {
                    if (RoleModel.ROLE_STUDENT.equals(roleModel)) {
                        return new StudentNotFoundException();
                    }
                    return new LecturerNotFoundException(id);
                });

        if (Boolean.TRUE.equals(user.getActive())) {
            user.setActive(false);
            userRepository.save(user);
        }
    }

    @Override
    public User update(UserUpdateRequest request, RoleModel roleModel) {
        TSID userId = TSID.from(request.getId());
        return userRepository.findByIdAndActiveTrueAndRole_Name(userId.toLong(), roleModel.getRoleName())
                .map(user -> {
                    String email = request.getEmail();
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    if (!email.equals(user.getEmail()) && userRepository.existsByEmail(email)) {
                        throw new EmailTakenException(email);
                    }
                    user.setEmail(email);
                    return user;
                })
                .map(userRepository::save)
                .orElseThrow(() -> {
                    if (RoleModel.ROLE_STUDENT.equals(roleModel)) {
                        return new StudentNotFoundException();
                    }
                    return new LecturerNotFoundException(userId.toString());
                });
    }
}
