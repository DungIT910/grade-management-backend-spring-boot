package com.boolfly.GradeManagementRestful.mapper;

import com.boolfly.GradeManagementRestful.api.dto.user.StudentResponse;
import com.boolfly.GradeManagementRestful.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    StudentResponse toStudentResponse(User user);
}
