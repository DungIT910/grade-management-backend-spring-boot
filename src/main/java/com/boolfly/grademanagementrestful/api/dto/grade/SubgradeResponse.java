package com.boolfly.grademanagementrestful.api.dto.grade;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubgradeResponse {
    private String subgradeId;
    private String subcolId;
    private String subcolName;
    private Double grade;
}
