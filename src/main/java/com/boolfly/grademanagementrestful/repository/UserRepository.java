package com.boolfly.grademanagementrestful.repository;

import com.boolfly.grademanagementrestful.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {
    Optional<User> findByEmail(String email);

    Optional<User> findByIdAndActiveTrue(Long id);

    List<User> findAllByIdInAndActiveTrue(List<Long> ids);

    Optional<User> findByIdAndRole_Name(Long id, String roleName);

    boolean existsByEmail(String email);

    Optional<User> findByIdAndActiveTrueAndRole_Name(Long id, String roleName);
}
