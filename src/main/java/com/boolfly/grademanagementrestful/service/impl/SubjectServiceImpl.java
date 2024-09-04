package com.boolfly.grademanagementrestful.service.impl;

import com.boolfly.grademanagementrestful.api.dto.subject.SearchSubjectRequest;
import com.boolfly.grademanagementrestful.api.dto.subject.SubjectAddRequest;
import com.boolfly.grademanagementrestful.api.dto.subject.SubjectUpdateRequest;
import com.boolfly.grademanagementrestful.builder.base.SearchParamsBuilder;
import com.boolfly.grademanagementrestful.builder.subject.SubjectSearchParamsBuilder;
import com.boolfly.grademanagementrestful.domain.Subject;
import com.boolfly.grademanagementrestful.domain.model.subject.SubjectStatus;
import com.boolfly.grademanagementrestful.exception.subject.SubjectNotFoundException;
import com.boolfly.grademanagementrestful.repository.SubjectRepository;
import com.boolfly.grademanagementrestful.service.SubjectService;
import com.querydsl.core.types.dsl.BooleanExpression;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;

    @Override
    public Page<Subject> getSubjects(int page, int size, SearchSubjectRequest request) {
        SearchParamsBuilder builder = SubjectSearchParamsBuilder.from(page, size, request);
        Optional<BooleanExpression> expression = builder.getCommonCriteria();
        Pageable pageable = builder.getPageable();
        return expression.map(ex -> subjectRepository.findAll(ex, pageable))
                .orElseGet(() -> subjectRepository.findAll(pageable));
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Subject addSubject(SubjectAddRequest request) {
        return subjectRepository.save(Subject.builder()
                .id(TSID.fast().toLong())
                .name(request.getName())
                .build());
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Subject updateSubject(SubjectUpdateRequest request) {
        return subjectRepository.findByIdAndStatus(TSID.from(request.getSubjectId()).toLong(), SubjectStatus.ACTIVE)
                .map(subject -> {
                    Optional.ofNullable(request.getName())
                            .filter(name -> !name.isEmpty() && !Objects.equals(subject.getName(), name))
                            .ifPresent(subject::setName);
                    return subjectRepository.save(subject);
                }).orElseThrow(() -> new SubjectNotFoundException(request.getSubjectId()));
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deactivateSubject(String subjectId) {
        Subject subject = subjectRepository.findByIdAndStatus(TSID.from(subjectId).toLong(), SubjectStatus.ACTIVE)
                .orElseThrow(() -> new SubjectNotFoundException(subjectId));

        subject.setStatus(SubjectStatus.INACTIVE);
        subjectRepository.save(subject);
    }
}
