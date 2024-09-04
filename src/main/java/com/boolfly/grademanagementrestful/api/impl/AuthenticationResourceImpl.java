package com.boolfly.grademanagementrestful.api.impl;

import com.boolfly.grademanagementrestful.api.AuthenticationResource;
import com.boolfly.grademanagementrestful.api.dto.auth.AuthenticationRequest;
import com.boolfly.grademanagementrestful.api.dto.auth.AuthenticationResponse;
import com.boolfly.grademanagementrestful.api.dto.user.UserResponse;
import com.boolfly.grademanagementrestful.mapper.UserMapper;
import com.boolfly.grademanagementrestful.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationResourceImpl implements AuthenticationResource {
    private static final UserMapper userMapper = UserMapper.INSTANCE;
    private final AuthenticationService authenticationService;

    @PostMapping("/token")
    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        return userMapper.toAuthenticationResponse(authenticationService.authenticate(request));
    }

    @GetMapping("/currentUser")
    @Override
    public UserResponse getCurrentUser() {
        return userMapper.toUserResponse(authenticationService.getCurrentUser());
    }
}
