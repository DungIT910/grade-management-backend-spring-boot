package com.boolfly.grademanagementrestful.service;

import com.boolfly.grademanagementrestful.api.dto.subcol.SearchSubcolRequest;
import com.boolfly.grademanagementrestful.api.dto.subcol.SubcolAddRequest;
import com.boolfly.grademanagementrestful.api.dto.subcol.SubcolUpdateRequest;
import com.boolfly.grademanagementrestful.domain.Subcol;
import org.springframework.data.domain.Page;

public interface SubcolService {
    Page<Subcol> getSubcols(int page, int size, SearchSubcolRequest request);

    Subcol addSubcol(SubcolAddRequest request);

    Subcol updateSubcol(SubcolUpdateRequest request);

    void deactivateSubcol(String subcolId);
}
