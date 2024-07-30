package com.boolfly.grademanagementrestful.mapper;

import com.boolfly.grademanagementrestful.api.dto.subcol.SubcolResponse;
import com.boolfly.grademanagementrestful.domain.Subcol;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubcolMapper extends TSIDMapper {
    SubcolMapper INSTANCE = Mappers.getMapper(SubcolMapper.class);

    @Mapping(target = "subcolId", source = "id", qualifiedByName = "toTSIDString")
    @Mapping(target = "courseId", source = "course.id", qualifiedByName = "toTSIDString")
    @Mapping(target = "courseName", source = "course.name")
    SubcolResponse toSubcolResponse(Subcol subcol);
}
