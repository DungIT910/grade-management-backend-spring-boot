package com.boolfly.grademanagementrestful.mapper;

import com.boolfly.grademanagementrestful.api.dto.subject.SubjectResponse;
import com.boolfly.grademanagementrestful.domain.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubjectMapper extends TSIDMapper {
    SubjectMapper INSTANCE = Mappers.getMapper(SubjectMapper.class);

    @Mapping(target = "subjectId", source = "id", qualifiedByName = "toTSIDString")
    SubjectResponse toSubjectResponse(Subject subject);
}
