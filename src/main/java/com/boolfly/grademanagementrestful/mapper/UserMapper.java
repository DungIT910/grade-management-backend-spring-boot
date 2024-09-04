package com.boolfly.grademanagementrestful.mapper;

import com.boolfly.grademanagementrestful.api.dto.auth.AuthenticationResponse;
import com.boolfly.grademanagementrestful.api.dto.course.student.CourseStudentResponse;
import com.boolfly.grademanagementrestful.api.dto.user.LecturerResponse;
import com.boolfly.grademanagementrestful.api.dto.user.StudentResponse;
import com.boolfly.grademanagementrestful.api.dto.user.UserResponse;
import com.boolfly.grademanagementrestful.domain.Role;
import com.boolfly.grademanagementrestful.domain.User;
import com.boolfly.grademanagementrestful.domain.model.role.RoleModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper extends TSIDMapper, NameMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Named("toRoleName")
    static String toRoleName(Role role) {
        try {
            return RoleModel.valueOf(role.getName()).getRoleType();
        } catch (IllegalArgumentException e) {
            return "UNDEFINED";
        }
    }

    @Mapping(target = "role", source = "role", qualifiedByName = "toRoleName")
    StudentResponse toStudentResponse(User user);

    @Mapping(target = "studentId", source = "id", qualifiedByName = "toTSIDString")
    @Mapping(target = "studentName", source = "user", qualifiedByName = "toUserName")
    CourseStudentResponse toCourseStudentResponse(User user);

    @Mapping(target = "role", source = "role", qualifiedByName = "toRoleName")
    LecturerResponse toLecturerResponse(User user);

    @Mapping(target = "accessToken", source = "accessToken")
    AuthenticationResponse toAuthenticationResponse(String accessToken);

    @Mapping(target = "role", source = "role", qualifiedByName = "toRoleName")
    UserResponse toUserResponse(User user);
}
