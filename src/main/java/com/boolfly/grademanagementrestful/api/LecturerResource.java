package com.boolfly.grademanagementrestful.api;

import com.boolfly.grademanagementrestful.api.dto.general.PageResponse;
import com.boolfly.grademanagementrestful.api.dto.user.LecturerAddRequest;
import com.boolfly.grademanagementrestful.api.dto.user.LecturerResponse;
import com.boolfly.grademanagementrestful.api.dto.user.LecturerUpdateRequest;
import com.boolfly.grademanagementrestful.api.dto.user.SearchUserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Lecturer Resource")
@Validated
public interface LecturerResource {
    @Operation(summary = "Add a new lecturer")
    LecturerResponse addLecturer(@Valid @RequestPart LecturerAddRequest request,
                                 @RequestPart MultipartFile avatar);

    @Operation(summary = "Get all lecturers")
    PageResponse<LecturerResponse> getLecturers(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                @RequestParam(defaultValue = "10") @Min(1) int size,
                                                @RequestBody SearchUserRequest request);

    @Operation(summary = "Update a lecturer")
    LecturerResponse updateLecturer(@Valid @RequestBody LecturerUpdateRequest request);

    @Operation(summary = "Deactivate a lecturer")
    void deactivateLecturer(String lecturerId);
}
