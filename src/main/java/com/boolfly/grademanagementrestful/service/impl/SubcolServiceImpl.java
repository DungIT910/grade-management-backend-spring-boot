package com.boolfly.grademanagementrestful.service.impl;

import com.boolfly.grademanagementrestful.api.dto.subcol.SearchSubcolRequest;
import com.boolfly.grademanagementrestful.api.dto.subcol.SubcolAddRequest;
import com.boolfly.grademanagementrestful.api.dto.subcol.SubcolUpdateRequest;
import com.boolfly.grademanagementrestful.builder.base.SearchParamsBuilder;
import com.boolfly.grademanagementrestful.builder.subcol.SubcolSearchParamsBuilder;
import com.boolfly.grademanagementrestful.domain.Course;
import com.boolfly.grademanagementrestful.domain.Subcol;
import com.boolfly.grademanagementrestful.domain.model.course.CourseStatus;
import com.boolfly.grademanagementrestful.domain.model.subcol.SubcolStatus;
import com.boolfly.grademanagementrestful.exception.course.CourseNotFoundException;
import com.boolfly.grademanagementrestful.exception.subcol.SubcolLimitException;
import com.boolfly.grademanagementrestful.exception.subcol.SubcolNotFoundException;
import com.boolfly.grademanagementrestful.repository.CourseRepository;
import com.boolfly.grademanagementrestful.repository.SubcolRepository;
import com.boolfly.grademanagementrestful.service.SubcolService;
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
public class SubcolServiceImpl implements SubcolService {
    private final CourseRepository courseRepository;
    private final SubcolRepository subcolRepository;

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<Subcol> getSubcols(int page, int size, SearchSubcolRequest request) {
        SearchParamsBuilder paramsBuilder = SubcolSearchParamsBuilder.from(page, size, request);
        Optional<BooleanExpression> predicate = paramsBuilder.getCommonCriteria();
        Pageable pageable = paramsBuilder.getPageable();
        return predicate.map(pre -> subcolRepository.findAll(pre, pageable))
                .orElseGet(() -> subcolRepository.findAll(pageable));
    }

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_LECTURER')")
    public Subcol addSubcol(SubcolAddRequest request) {
        String courseIdAsString = request.getCourseId();
        Long courseId = TSID.from(courseIdAsString).toLong();
        Course course = courseRepository.findByIdAndStatusNot(courseId, CourseStatus.INACTIVE)
                .orElseThrow(() -> new CourseNotFoundException(courseIdAsString));

        if (subcolRepository.countByCourseIdAndStatus(courseId, SubcolStatus.ACTIVE) >= 3) {
            throw new SubcolLimitException();
        }

        return subcolRepository.save(
                Subcol.builder()
                        .id(TSID.fast().toLong())
                        .name(request.getSubcolName())
                        .course(course)
                        .status(SubcolStatus.ACTIVE)
                        .build());
    }

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_LECTURER')")
    public Subcol updateSubcol(SubcolUpdateRequest request) {
        String subcolIdAsString = request.getSubcolId();
        return subcolRepository.findByIdAndStatus(TSID.from(subcolIdAsString).toLong(), SubcolStatus.ACTIVE)
                .map(subcol -> {
                    Optional.ofNullable(request.getSubcolName())
                            .filter(name -> !name.isEmpty() && !Objects.equals(name, subcol.getName()))
                            .ifPresent(subcol::setName);
                    return subcolRepository.save(subcol);
                }).orElseThrow(() -> new SubcolNotFoundException(subcolIdAsString));
    }

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_LECTURER')")
    public void deactivateSubcol(String subcolId) {
        Subcol subcol = subcolRepository.findByIdAndStatus(TSID.from(subcolId).toLong(), SubcolStatus.ACTIVE)
                .orElseThrow(() -> new SubcolNotFoundException(subcolId));

        subcol.setStatus(SubcolStatus.INACTIVE);
        subcolRepository.save(subcol);
    }
}
