package com.boolfly.grademanagementrestful.repository.custom;

import com.boolfly.grademanagementrestful.repository.custom.model.PairSubgradeSubcol;

import java.util.List;

public interface CustomSubgradeRepository {
    List<PairSubgradeSubcol> findAllByCourseIdAndStudentIdAndActive(Long courseId, Long studentId);
}
