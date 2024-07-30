package com.boolfly.grademanagementrestful.api.dto.user;

import com.boolfly.grademanagementrestful.domain.model.role.RoleModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.List;

@Getter
@With
@NoArgsConstructor
@AllArgsConstructor
public final class SearchUserRequest {
    private List<RoleModel> roles;
    private String firstName;
    private String lastName;
    private String userId;

    public List<RoleModel> getRoles() {
        if (roles == null || roles.isEmpty()) {
            return List.of();
        }

        return List.copyOf(roles);
    }
}
