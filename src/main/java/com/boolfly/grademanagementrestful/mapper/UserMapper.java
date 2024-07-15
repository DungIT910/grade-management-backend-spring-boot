package com.boolfly.grademanagementrestful.mapper;

import com.boolfly.grademanagementrestful.api.dto.user.StudentResponse;
import com.boolfly.grademanagementrestful.domain.User;
import io.hypersistence.tsid.TSID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Named("toStudentId")
    static String toStudentId(Long id) {
        TSID tsid = TSID.from(id);

        return tsid.toString();
    }

    @Mapping(target = "studentId", source = "id", qualifiedByName = "toStudentId")
    StudentResponse toStudentResponse(User user);
}
