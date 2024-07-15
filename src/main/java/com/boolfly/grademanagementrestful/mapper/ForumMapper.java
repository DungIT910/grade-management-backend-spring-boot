package com.boolfly.grademanagementrestful.mapper;

import com.boolfly.grademanagementrestful.api.dto.forum.ForumResponse;
import com.boolfly.grademanagementrestful.domain.Forum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ForumMapper extends TSIDMapper {
    ForumMapper INSTANCE = Mappers.getMapper(ForumMapper.class);

    @Mapping(target = "forumId", source = "id", qualifiedByName = "toTSIDString")
    @Mapping(target = "courseId", source = "course.id", qualifiedByName = "toTSIDString")
    @Mapping(target = "courseName", source = "course.name")
    ForumResponse toForumResponse(Forum forum);
}
