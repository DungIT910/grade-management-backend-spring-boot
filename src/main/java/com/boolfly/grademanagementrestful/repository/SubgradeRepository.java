package com.boolfly.grademanagementrestful.repository;

import com.boolfly.grademanagementrestful.domain.Subgrade;
import com.boolfly.grademanagementrestful.repository.custom.CustomSubgradeRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubgradeRepository extends JpaRepository<Subgrade, Long>, CustomSubgradeRepository {
    Optional<Subgrade> findBySubcol_IdAndStudent_Id(Long subgradeId, Long studentId);
}
