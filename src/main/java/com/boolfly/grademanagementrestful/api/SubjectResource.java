package com.boolfly.grademanagementrestful.api;

import com.boolfly.grademanagementrestful.api.dto.general.PageResponse;
import com.boolfly.grademanagementrestful.api.dto.subject.SearchSubjectRequest;
import com.boolfly.grademanagementrestful.api.dto.subject.SubjectAddRequest;
import com.boolfly.grademanagementrestful.api.dto.subject.SubjectResponse;
import com.boolfly.grademanagementrestful.api.dto.subject.SubjectUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Subject Resource")
@Validated
public interface SubjectResource {
    @Operation(summary = "Get all subjects")
    PageResponse<SubjectResponse> getSubjects(@RequestParam(defaultValue = "0") @Min(0) int page,
                                              @RequestParam(defaultValue = "10") @Min(1) int size,
                                              @RequestBody SearchSubjectRequest request);

    @Operation(summary = "add new subject")
    SubjectResponse addSubject(@Valid @RequestBody SubjectAddRequest request);

    @Operation(summary = "update a subject")
    SubjectResponse updateSubject(@Valid @RequestBody SubjectUpdateRequest request);

    @Operation(summary = "deactivate a subject")
    void deactivateSubject(String subjectId);
}