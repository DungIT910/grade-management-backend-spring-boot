package com.boolfly.grademanagementrestful.mapper;

import com.boolfly.grademanagementrestful.api.dto.subject.SubjectResponse;
import com.boolfly.grademanagementrestful.domain.Subject;
import io.hypersistence.tsid.TSID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubjectMapper {
    SubjectMapper INSTANCE = Mappers.getMapper(SubjectMapper.class);

    @Named("toSubjectId")
    static String toSubjectId(Long id) {
        TSID tsid = TSID.from(id);
        return tsid.toString();
    }

    @Mapping(target = "subjectId", source = "id", qualifiedByName = "toSubjectId")
    SubjectResponse toSubjectResponse(Subject subject);
}
