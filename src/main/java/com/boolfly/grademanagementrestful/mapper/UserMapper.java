package com.boolfly.grademanagementrestful.mapper;

import com.boolfly.grademanagementrestful.api.dto.course.student.CourseStudentResponse;
import com.boolfly.grademanagementrestful.api.dto.user.LecturerResponse;
import com.boolfly.grademanagementrestful.api.dto.user.StudentResponse;
import com.boolfly.grademanagementrestful.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper extends TSIDMapper, NameMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "studentId", source = "id", qualifiedByName = "toTSIDString")
    StudentResponse toStudentResponse(User user);

    @Mapping(target = "studentId", source = "id", qualifiedByName = "toTSIDString")
    @Mapping(target = "studentName", source = "user", qualifiedByName = "toUserName")
    CourseStudentResponse toCourseStudentResponse(User user);

    @Mapping(target = "lecturerId", source = "id", qualifiedByName = "toTSIDString")
    LecturerResponse toLecturerResponse(User user);
}
