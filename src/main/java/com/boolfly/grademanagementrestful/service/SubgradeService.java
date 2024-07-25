package com.boolfly.grademanagementrestful.service;

import com.boolfly.grademanagementrestful.domain.Maingrade;
import com.boolfly.grademanagementrestful.repository.custom.model.PairSubgradeSubcol;

import java.util.List;
import java.util.Map;

public interface SubgradeService {
    Map<String, List<PairSubgradeSubcol>> getPairSubgradeSubcol(List<Maingrade> maingradeList);
}
