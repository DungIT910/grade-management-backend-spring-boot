package com.boolfly.grademanagementrestful.security.custom;

import com.boolfly.grademanagementrestful.domain.User;
import com.boolfly.grademanagementrestful.domain.model.course.CourseStatus;
import com.boolfly.grademanagementrestful.domain.model.forum.ForumStatus;
import com.boolfly.grademanagementrestful.domain.model.maingrade.MaingradeStatus;
import com.boolfly.grademanagementrestful.domain.model.post.PostStatus;
import com.boolfly.grademanagementrestful.domain.model.role.RoleModel;
import com.boolfly.grademanagementrestful.domain.model.subcol.SubcolStatus;
import com.boolfly.grademanagementrestful.exception.comment.CommentNotFoundException;
import com.boolfly.grademanagementrestful.exception.forum.ForumNotFoundException;
import com.boolfly.grademanagementrestful.exception.subcol.SubcolNotFoundException;
import com.boolfly.grademanagementrestful.exception.user.UserNotFoundException;
import com.boolfly.grademanagementrestful.repository.*;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomSecurityExpression {

    private final MaingradeRepository maingradeRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final SubcolRepository subcolRepository;
    private final ForumRepository forumRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public boolean verifyUserInCourseByCourseId(String email, String courseId) {
        return checkUserInCourseByCourseId(email, TSID.from(courseId).toLong());
    }

    public boolean checkUserInCourseByCourseId(String email, Long courseId) {
        User user = userRepository.findByEmailAndActiveTrue(email)
                .orElseThrow(UserNotFoundException::new);

        if (user.getRole().getName().equals(RoleModel.ROLE_ADMIN.getRoleName())) {
            return true;
        } else if (user.getRole().getName().equals(RoleModel.ROLE_LECTURER.getRoleName())) {
            return courseRepository.existsByIdAndLecturer_IdAndStatusNot(
                    courseId, user.getId(), CourseStatus.INACTIVE
            );
        } else if (user.getRole().getName().equals(RoleModel.ROLE_STUDENT.getRoleName())) {
            return maingradeRepository.existsByCourse_IdAndStudent_IdAndStatusNot(
                    courseId, user.getId(), MaingradeStatus.INACTIVE
            );
        }
        return false;
    }

    public boolean verifyUserInCourseByForumId(String email, String forumId) {
        Long forumLongId = TSID.from(forumId).toLong();

        return forumRepository.findCourseIdByForumIdAndStatus(forumLongId, ForumStatus.ACTIVE)
                .map(courseId -> checkUserInCourseByCourseId(email, courseId))
                .orElseThrow(() -> new ForumNotFoundException(forumId));
    }

    public boolean verifyUserInCourseByPostId(String email, String postId) {
        Long postLongId = TSID.from(postId).toLong();

        return postRepository.findCourseIdByPostIdAndStatus(postLongId, PostStatus.ACTIVE)
                .map(courseId -> checkUserInCourseByCourseId(email, courseId))
                .orElseThrow(() -> new ForumNotFoundException(postId));
    }

    public boolean verifyUserInCourseByCommentId(String email, Long commentId) {
        return commentRepository.findCourseIdByCommentId(commentId)
                .map(courseId -> checkUserInCourseByCourseId(email, courseId))
                .orElseThrow(CommentNotFoundException::new);
    }

    public boolean verifyUserInCourseBySubcolId(String email, Long subcolId) {
        return subcolRepository.findCourseIdBySubcol_IdAndStatus(subcolId, SubcolStatus.ACTIVE)
                .map(courseId -> checkUserInCourseByCourseId(email, courseId))
                .orElseThrow(() -> new SubcolNotFoundException(TSID.from(subcolId).toString()));
    }

    public boolean verifyCommentOwner(String email, Long commentId) {
        return commentRepository.isCommentOwner(commentId, email);
    }

    public boolean verifyPostOwner(String email, Long postId) {
        return postRepository.isPostOwner(postId, email);
    }

    public boolean verifyAccountOwner(String email, String userId) {
        return userRepository.isAccountOwner(TSID.from(userId).toLong(), email);
    }
}
