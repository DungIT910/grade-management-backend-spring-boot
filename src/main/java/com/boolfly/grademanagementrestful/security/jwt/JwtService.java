package com.boolfly.grademanagementrestful.security.jwt;

import com.boolfly.grademanagementrestful.domain.User;

public interface JwtService {
    String generateAccessToken(User user);

    String getUsernameFromToken(String token);
}
