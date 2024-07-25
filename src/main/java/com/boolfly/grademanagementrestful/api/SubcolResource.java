package com.boolfly.grademanagementrestful.api;

import com.boolfly.grademanagementrestful.api.dto.general.PageResponse;
import com.boolfly.grademanagementrestful.api.dto.subcol.SearchSubcolRequest;
import com.boolfly.grademanagementrestful.api.dto.subcol.SubcolAddRequest;
import com.boolfly.grademanagementrestful.api.dto.subcol.SubcolResponse;
import com.boolfly.grademanagementrestful.api.dto.subcol.SubcolUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Subcol Resource")
@Validated
public interface SubcolResource {
    @Operation(summary = "Get all subcols")
    PageResponse<SubcolResponse> getSubcols(@RequestParam(defaultValue = "0") @Min(0) int page,
                                            @RequestParam(defaultValue = "10") @Min(1) int size,
                                            @RequestBody SearchSubcolRequest request);


    @Operation(summary = "Add a new subcol")
    SubcolResponse addSubcol(@Valid @RequestBody SubcolAddRequest request);

    @Operation(summary = "Update a subcol")
    SubcolResponse updateSubcol(@Valid @RequestBody SubcolUpdateRequest request);

    @Operation(summary = "Deactivate a subcol")
    void deactivateSubcol(String subcolId);
}
