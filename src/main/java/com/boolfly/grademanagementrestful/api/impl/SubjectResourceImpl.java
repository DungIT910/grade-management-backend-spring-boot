package com.boolfly.grademanagementrestful.api.impl;

import com.boolfly.grademanagementrestful.api.SubjectResource;
import com.boolfly.grademanagementrestful.api.dto.general.PageResponse;
import com.boolfly.grademanagementrestful.api.dto.subject.SearchSubjectRequest;
import com.boolfly.grademanagementrestful.api.dto.subject.SubjectAddRequest;
import com.boolfly.grademanagementrestful.api.dto.subject.SubjectResponse;
import com.boolfly.grademanagementrestful.api.dto.subject.SubjectUpdateRequest;
import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;
import com.boolfly.grademanagementrestful.mapper.SubjectMapper;
import com.boolfly.grademanagementrestful.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subjects")
@RequiredArgsConstructor
public class SubjectResourceImpl implements SubjectResource {
    private static final SubjectMapper subjectMapper = SubjectMapper.INSTANCE;
    private final SubjectService subjectService;

    @PostMapping("/search")
    @Override
    public PageResponse<SubjectResponse> getSubjects(int page, int size, SearchSubjectRequest request) {
        Page<SubjectResponse> pageSubject = subjectService.getSubjects(page, size, request).map(subjectMapper::toSubjectResponse);
        PageResponse<SubjectResponse> pageSubjectResponse = new PageResponse<>();
        pageSubjectResponse.setContent(pageSubject.getContent());
        return pageSubjectResponse;
    }

    @PostMapping
    @Override
    public SubjectResponse addSubject(SubjectAddRequest request) {
        try {
            return subjectMapper.toSubjectResponse(subjectService.addSubject(request));
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }

    @PutMapping
    @Override
    public SubjectResponse updateSubject(SubjectUpdateRequest request) {
        try {
            return subjectMapper.toSubjectResponse(subjectService.updateSubject(request));
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }

    @DeleteMapping("/{subjectId}")
    @Override
    public void deactivateSubject(@PathVariable String subjectId) {
        try {
            subjectService.deactivateSubject(subjectId);
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }
}
