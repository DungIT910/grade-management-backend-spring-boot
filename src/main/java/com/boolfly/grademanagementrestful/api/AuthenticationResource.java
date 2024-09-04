package com.boolfly.grademanagementrestful.api;

import com.boolfly.grademanagementrestful.api.dto.auth.AuthenticationRequest;
import com.boolfly.grademanagementrestful.api.dto.auth.AuthenticationResponse;
import com.boolfly.grademanagementrestful.api.dto.user.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Authentication Resource")
@Validated
public interface AuthenticationResource {
    @Operation(summary = "Authenticate specific user, return access and refresh token")
    AuthenticationResponse authenticate(@Valid @RequestBody AuthenticationRequest request);

    @Operation(summary = "Get current authenticated user")
    UserResponse getCurrentUser();
}
