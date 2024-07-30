package com.boolfly.grademanagementrestful.repository.custom.model;

import com.boolfly.grademanagementrestful.domain.Subcol;
import com.boolfly.grademanagementrestful.domain.Subgrade;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PairSubgradeSubcol {
    private Subgrade subgrade;
    private Subcol subcol;
}
