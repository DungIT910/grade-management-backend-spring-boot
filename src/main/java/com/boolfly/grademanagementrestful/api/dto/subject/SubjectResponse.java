package com.boolfly.grademanagementrestful.api.dto.subject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectResponse {
    private String subjectId;
    private String name;
    private String status;
}
