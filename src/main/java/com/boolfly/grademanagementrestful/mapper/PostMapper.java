package com.boolfly.grademanagementrestful.mapper;

import com.boolfly.grademanagementrestful.api.dto.post.PostResponse;
import com.boolfly.grademanagementrestful.domain.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostMapper extends TSIDMapper, NameMapper, TimeStringMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(target = "postId", source = "id", qualifiedByName = "toTSIDString")
    @Mapping(target = "forumId", source = "forum.id", qualifiedByName = "toTSIDString")
    @Mapping(target = "forumName", source = "forum.name")
    @Mapping(target = "createdById", source = "createdBy.id", qualifiedByName = "toTSIDString")
    @Mapping(target = "createdByName", source = "createdBy", qualifiedByName = "toUserName")
    @Mapping(target = "updatedById", source = "updatedBy.id", qualifiedByName = "toTSIDString")
    @Mapping(target = "updatedByName", source = "updatedBy", qualifiedByName = "toUserName")
    @Mapping(target = "createdOn", source = "createdOn", qualifiedByName = "toStringWithFormat")
    @Mapping(target = "updatedOn", source = "updatedOn", qualifiedByName = "toStringWithFormat")
    PostResponse toPostResponse(Post post);
}
