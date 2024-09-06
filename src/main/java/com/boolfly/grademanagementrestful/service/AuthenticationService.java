package com.boolfly.grademanagementrestful.service;

import com.boolfly.grademanagementrestful.api.dto.auth.AuthenticationRequest;
import com.boolfly.grademanagementrestful.domain.User;

public interface AuthenticationService {
    String authenticate(AuthenticationRequest request);

    User getCurrentUser();
}
