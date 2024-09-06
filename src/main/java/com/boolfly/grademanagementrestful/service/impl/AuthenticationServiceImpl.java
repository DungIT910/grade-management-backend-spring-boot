package com.boolfly.grademanagementrestful.service.impl;

import com.boolfly.grademanagementrestful.api.dto.auth.AuthenticationRequest;
import com.boolfly.grademanagementrestful.domain.User;
import com.boolfly.grademanagementrestful.exception.user.FailedAuthenticationException;
import com.boolfly.grademanagementrestful.exception.user.UserNotFoundException;
import com.boolfly.grademanagementrestful.repository.UserRepository;
import com.boolfly.grademanagementrestful.security.jwt.JwtService;
import com.boolfly.grademanagementrestful.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public String authenticate(AuthenticationRequest request) {
        String userName = request.getUsername();
        User user = userRepository.findByEmailAndActiveTrue(userName)
                .orElseThrow(UserNotFoundException::new);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new FailedAuthenticationException();
        }
        return jwtService.generateAccessToken(user);
    }

    @Override
    public User getCurrentUser() {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        return userRepository.findByEmailAndActiveTrue(username)
                .orElseThrow(UserNotFoundException::new);
    }
}
