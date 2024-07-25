package com.boolfly.grademanagementrestful.api;

import com.boolfly.grademanagementrestful.api.dto.general.BatchRequest;
import com.boolfly.grademanagementrestful.api.dto.general.BatchResponse;
import com.boolfly.grademanagementrestful.api.dto.general.PageResponse;
import com.boolfly.grademanagementrestful.api.dto.grade.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Grade Resource")
public interface GradeResource {
    @Operation(summary = "Get all grades")
    PageResponse<GradeResponse> getGrades(@RequestParam(defaultValue = "0") @Min(0) int page,
                                          @RequestParam(defaultValue = "10") @Min(1) int size,
                                          @RequestBody SearchGradeRequest request);

    @Operation(summary = "Update main grades of a student")
    MaingradeDetailsResponse updateMaingrade(@RequestBody @Valid MaingradeUpdateRequest request);

    @Operation(summary = "Update sub-grades")
    SubgradeDetailsResponse updateSubgrade(@RequestBody @Valid SubgradeUpdateRequest request);

    @Operation(summary = "Update a batch of maingrade")
    BatchResponse<MaingradeDetailsResponse> updateMaingradeBatch(@RequestBody @Valid BatchRequest<MaingradeUpdateRequest> request);

    @Operation(summary = "Update a batch of subgrade")
    BatchResponse<SubgradeDetailsResponse> updateSubgradeBatch(@RequestBody @Valid BatchRequest<SubgradeUpdateRequest> request);
}
