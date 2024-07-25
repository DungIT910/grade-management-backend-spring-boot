package com.boolfly.grademanagementrestful.api.impl;

import com.boolfly.grademanagementrestful.api.GradeResource;
import com.boolfly.grademanagementrestful.api.dto.general.BatchRequest;
import com.boolfly.grademanagementrestful.api.dto.general.BatchResponse;
import com.boolfly.grademanagementrestful.api.dto.general.PageResponse;
import com.boolfly.grademanagementrestful.api.dto.grade.*;
import com.boolfly.grademanagementrestful.domain.Maingrade;
import com.boolfly.grademanagementrestful.mapper.GradeMapper;
import com.boolfly.grademanagementrestful.mapper.SubgradeMapper;
import com.boolfly.grademanagementrestful.repository.custom.model.PairSubgradeSubcol;
import com.boolfly.grademanagementrestful.service.GradeService;
import com.boolfly.grademanagementrestful.service.SubgradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/grades")
@RequiredArgsConstructor
public class GradeResourceImpl implements GradeResource {
    private static final GradeMapper gradeMapper = GradeMapper.INSTANCE;
    private static final SubgradeMapper subgradeMapper = SubgradeMapper.INSTANCE;
    private final GradeService gradeService;
    private final SubgradeService subgradeService;

    @PostMapping("/search")
    @Override
    public PageResponse<GradeResponse> getGrades(int page, int size, SearchGradeRequest request) {
        Page<Maingrade> pageMaingrade = gradeService.getGrades(page, size, request);
        List<Maingrade> maingradeList = pageMaingrade.getContent();
        Map<String, List<PairSubgradeSubcol>> mapPairSubgradeSubcol = subgradeService.getPairSubgradeSubcol(maingradeList);

        return PageResponse.<GradeResponse>builder()
                .content(maingradeList.stream()
                        .map(gradeMapper::toGradeResponse)
                        .map(grade -> {
                            String maingradeId = grade.getMaingradeId();
                            List<SubgradeResponse> subgradeResponseList = mapPairSubgradeSubcol.get(maingradeId)
                                    .stream()
                                    .map(subgradeMapper::toSubgradeResponse)
                                    .toList();
                            return grade.withSubgradeList(subgradeResponseList);
                        })
                        .toList())
                .build();
    }

    @PutMapping("/maingrades")
    @Override
    public MaingradeDetailsResponse updateMaingrade(MaingradeUpdateRequest request) {
        return gradeMapper.toMaingradeDetailsResponse(gradeService.updateMaingrade(request));
    }

    @PutMapping("/maingrades/batch")
    @Override
    public BatchResponse<MaingradeDetailsResponse> updateMaingradeBatch(BatchRequest<MaingradeUpdateRequest> request) {
        BatchResponse<MaingradeDetailsResponse> response = new BatchResponse<>();
        List<MaingradeDetailsResponse> mgList = gradeService.updateMaingradeBatch(request).stream().map(gradeMapper::toMaingradeDetailsResponse).toList();
        response.setBatch(mgList);
        return response;
    }

    @PutMapping("/subgrades/batch")
    @Override
    public BatchResponse<SubgradeDetailsResponse> updateSubgradeBatch(BatchRequest<SubgradeUpdateRequest> request) {
        BatchResponse<SubgradeDetailsResponse> response = new BatchResponse<>();
        List<SubgradeDetailsResponse> sgList = gradeService.updateSubgradeBatch(request).stream().map(gradeMapper::toSubgradeDetailsResponse).toList();
        response.setBatch(sgList);
        return response;
    }

    @PutMapping("/subgrades")
    @Override
    public SubgradeDetailsResponse updateSubgrade(SubgradeUpdateRequest request) {
        return gradeMapper.toSubgradeDetailsResponse(gradeService.updateSubgrade(request));
    }
}
