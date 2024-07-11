package com.boolfly.grademanagementrestful.api.dto.subject;

import com.boolfly.grademanagementrestful.domain.model.subject.SubjectStatus;
import lombok.Getter;

import java.util.List;

@Getter
public class SearchSubjectRequest {
    private String subjectId;
    private String name;
    private List<SubjectStatus> status;
}
