package com.boolfly.grademanagementrestful.service.base;

import com.boolfly.grademanagementrestful.api.dto.user.UserRegistrationRequest;
import com.boolfly.grademanagementrestful.api.dto.user.UserUpdateRequest;
import com.boolfly.grademanagementrestful.domain.User;
import com.boolfly.grademanagementrestful.domain.model.role.RoleModel;

public interface UserService {
    User create(UserRegistrationRequest request, RoleModel roleModel);

    void deactivate(String id, RoleModel roleModel);

    User update(UserUpdateRequest request, RoleModel roleModel);

    User getUser(String id);
}
