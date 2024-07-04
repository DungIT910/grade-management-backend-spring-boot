package com.boolfly.GradeManagementRestful.service.impl;

import com.boolfly.GradeManagementRestful.api.dto.user.StudentRegistrationRequest;
import com.boolfly.GradeManagementRestful.domain.User;
import com.boolfly.GradeManagementRestful.exception.user.EmailTakenException;
import com.boolfly.GradeManagementRestful.exception.user.StudentNotFoundException;
import com.boolfly.GradeManagementRestful.repository.UserRepository;
import com.boolfly.GradeManagementRestful.service.UserService;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User createStudent(StudentRegistrationRequest request) {
        String email = request.getEmail();
        userRepository.findByEmail(email)
                .ifPresentOrElse(user -> {
                    throw new EmailTakenException(user.getEmail());
                }, () -> {
                    User user = new User();
                    user.setId(TSID.fast().toLong());
                    user.setEmail(email);
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    user.setPassword(request.getPassword());

                    userRepository.save(user);
                });
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new StudentNotFoundException(email));
    }

    @Override
    public Page<User> getStudents(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size));
    }
}
