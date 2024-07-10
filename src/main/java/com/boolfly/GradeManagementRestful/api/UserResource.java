package com.boolfly.GradeManagementRestful.api;

import com.boolfly.GradeManagementRestful.api.dto.general.PageResponse;
import com.boolfly.GradeManagementRestful.api.dto.user.SearchUserRequest;
import com.boolfly.GradeManagementRestful.api.dto.user.StudentRegistrationRequest;
import com.boolfly.GradeManagementRestful.api.dto.user.StudentResponse;
import com.boolfly.GradeManagementRestful.api.dto.user.StudentUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "User Resource")
@Validated
public interface UserResource {
    @Operation(summary = "Create a new student")
    StudentResponse createStudent(@Valid @RequestBody StudentRegistrationRequest request);

    @Operation(summary = "Get all students")
    PageResponse<StudentResponse> getStudents(@RequestParam(defaultValue = "0") @Min(0) int page,
                                              @RequestParam(defaultValue = "10") @Min(1) int size,
                                              @RequestBody SearchUserRequest request);

    @Operation(summary = "Update a student")
    StudentResponse updateStudent(@Valid @RequestBody StudentUpdateRequest request);

    @Operation(summary = "Deactivate a student")
    void deactivateStudent(String studentId);
}
