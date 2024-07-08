package com.boolfly.GradeManagementRestful.repository;

import com.boolfly.GradeManagementRestful.domain.Role;
import com.boolfly.GradeManagementRestful.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {
    Optional<User> findByEmail(String email);

    Page<User> findByRole(Role role, Pageable pageable);
}
