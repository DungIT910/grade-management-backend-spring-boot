package com.boolfly.GradeManagementRestful.repository;

import com.boolfly.GradeManagementRestful.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
}
