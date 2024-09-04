package com.boolfly.grademanagementrestful.security.custom;

import com.boolfly.grademanagementrestful.domain.User;
import com.boolfly.grademanagementrestful.domain.model.maingrade.MaingradeStatus;
import com.boolfly.grademanagementrestful.exception.user.UserNotFoundException;
import com.boolfly.grademanagementrestful.repository.MaingradeRepository;
import com.boolfly.grademanagementrestful.repository.UserRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomSecurityExpression {
    private final MaingradeRepository maingradeRepository;
    private final UserRepository userRepository;

    public boolean isStudentEnrolledInCourse(Authentication authentication, String courseId) {
        String email = authentication.name();
        Long courseLongId = TSID.from(courseId).toLong();
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        return maingradeRepository.existsByCourse_IdAndStudent_IdAndStatusNot(courseLongId, user.getId(), MaingradeStatus.INACTIVE);
    }
}
