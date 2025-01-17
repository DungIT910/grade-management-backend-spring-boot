package com.boolfly.grademanagementrestful.repository;

import com.boolfly.grademanagementrestful.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {
    Optional<User> findByIdAndActiveTrue(Long id);

    List<User> findAllByIdInAndActiveTrue(List<Long> ids);

    Optional<User> findByIdAndRole_Name(Long id, String roleName);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    Optional<User> findByEmailAndActiveTrue(String email);

    boolean existsByEmail(String email);

    Optional<User> findByIdAndActiveTrueAndRole_Name(Long id, String roleName);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.avatar = ?1 WHERE u.id = ?2 AND u.active IS TRUE")
    void updateAvatarByIdAndActiveTrue(String avatarUrl, Long id);

    @Query("SELECT EXISTS (SELECT 1 FROM User u WHERE u.id = :userId AND u.email = :email)")
    Boolean isAccountOwner(Long userId, String email);
}
