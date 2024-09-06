package com.boolfly.grademanagementrestful.api.impl;

import com.boolfly.grademanagementrestful.api.SubcolResource;
import com.boolfly.grademanagementrestful.api.dto.general.PageResponse;
import com.boolfly.grademanagementrestful.api.dto.subcol.SearchSubcolRequest;
import com.boolfly.grademanagementrestful.api.dto.subcol.SubcolAddRequest;
import com.boolfly.grademanagementrestful.api.dto.subcol.SubcolResponse;
import com.boolfly.grademanagementrestful.api.dto.subcol.SubcolUpdateRequest;
import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;
import com.boolfly.grademanagementrestful.mapper.SubcolMapper;
import com.boolfly.grademanagementrestful.service.SubcolService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subcols")
@RequiredArgsConstructor
public class SubcolResourceImpl implements SubcolResource {
    private static final SubcolMapper subcolMapper = SubcolMapper.INSTANCE;
    private final SubcolService subcolService;

    @PostMapping("/search")
    @Override
    public PageResponse<SubcolResponse> getSubcols(int page, int size, SearchSubcolRequest request) {
        Page<SubcolResponse> pageSubcol = subcolService.getSubcols(page, size, request).map(subcolMapper::toSubcolResponse);
        PageResponse<SubcolResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(pageSubcol.getContent());
        return pageResponse;
    }

    @PostMapping
    @PreAuthorize("@customSecurityExpression.verifyUserInCourseByCourseId(authentication.name, #request.courseId)")
    @Override
    public SubcolResponse addSubcol(SubcolAddRequest request) {
        try {
            return subcolMapper.toSubcolResponse(subcolService.addSubcol(request));
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }

    @PutMapping
    @PreAuthorize("@customSecurityExpression.verifyUserInCourseBySubcolId(authentication.name, #request.subcolId)")
    @Override
    public SubcolResponse updateSubcol(SubcolUpdateRequest request) {
        try {
            return subcolMapper.toSubcolResponse(subcolService.updateSubcol(request));
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }

    @DeleteMapping("/{subcolId}")
    @PreAuthorize("@customSecurityExpression.verifyUserInCourseBySubcolId(authentication.name, #subcolId)")
    @Override
    public void deactivateSubcol(@PathVariable String subcolId) {
        try {
            subcolService.deactivateSubcol(subcolId);
        } catch (Exception e) {
            throw new GradeManagementRuntimeException(e);
        }
    }
}
