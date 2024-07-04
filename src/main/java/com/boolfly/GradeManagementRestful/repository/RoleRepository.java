package com.boolfly.GradeManagementRestful.repository;

import com.boolfly.GradeManagementRestful.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
