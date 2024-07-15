package com.boolfly.grademanagementrestful.service;

import com.boolfly.grademanagementrestful.api.dto.subject.SearchSubjectRequest;
import com.boolfly.grademanagementrestful.api.dto.subject.SubjectAddRequest;
import com.boolfly.grademanagementrestful.api.dto.subject.SubjectUpdateRequest;
import com.boolfly.grademanagementrestful.domain.Subject;
import org.springframework.data.domain.Page;

public interface SubjectService {
    Page<Subject> getSubjects(int page, int size, SearchSubjectRequest request);

    Subject addSubject(SubjectAddRequest request);

    Subject updateSubject(SubjectUpdateRequest request);

    void deactivateSubject(String subjectId);
}
