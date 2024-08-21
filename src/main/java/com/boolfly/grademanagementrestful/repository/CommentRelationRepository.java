package com.boolfly.grademanagementrestful.repository;

import com.boolfly.grademanagementrestful.domain.CommentRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRelationRepository extends JpaRepository<CommentRelation, Long> {
    List<CommentRelation> findAllByDescendant_Id(Long id);
}
