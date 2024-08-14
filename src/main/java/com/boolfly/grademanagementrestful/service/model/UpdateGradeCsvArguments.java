package com.boolfly.grademanagementrestful.service.model;

import com.boolfly.grademanagementrestful.domain.Maingrade;
import com.boolfly.grademanagementrestful.domain.Subcol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class UpdateGradeCsvArguments {
    private Map<String, String> line;
    private List<Maingrade> maingradeList;
    private List<Subcol> subcolList;
}
