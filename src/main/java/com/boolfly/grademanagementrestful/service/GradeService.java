package com.boolfly.grademanagementrestful.service;

import com.boolfly.grademanagementrestful.api.dto.general.BatchRequest;
import com.boolfly.grademanagementrestful.api.dto.grade.MaingradeUpdateRequest;
import com.boolfly.grademanagementrestful.api.dto.grade.SearchGradeRequest;
import com.boolfly.grademanagementrestful.api.dto.grade.SubgradeUpdateRequest;
import com.boolfly.grademanagementrestful.domain.Maingrade;
import com.boolfly.grademanagementrestful.domain.Subgrade;
import com.boolfly.grademanagementrestful.service.base.PrepareDocumentData;
import com.boolfly.grademanagementrestful.service.model.UpdateGradeCsvArguments;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GradeService extends PrepareDocumentData {
    Page<Maingrade> getGrades(int page, int size, SearchGradeRequest request);

    Maingrade updateMaingrade(MaingradeUpdateRequest request);

    Subgrade updateSubgrade(SubgradeUpdateRequest request);

    List<Maingrade> updateMaingradeBatch(BatchRequest<MaingradeUpdateRequest> request);

    List<Subgrade> updateSubgradeBatch(BatchRequest<SubgradeUpdateRequest> request);

    void processUpdateGradeCsv(UpdateGradeCsvArguments updateGradeCsvArguments);
}
