package com.boolfly.grademanagementrestful.api.dto.subcol;

import com.boolfly.grademanagementrestful.domain.model.subcol.SubcolStatus;
import lombok.Getter;

import java.util.List;

@Getter
public class SearchSubcolRequest {
    private String subcolId;
    private String name;
    private String courseId;
    private List<SubcolStatus> status;
}
