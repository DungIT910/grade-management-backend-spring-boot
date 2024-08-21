package com.boolfly.grademanagementrestful.mapper;

import com.boolfly.grademanagementrestful.api.dto.comment.CommentResponse;
import com.boolfly.grademanagementrestful.domain.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper extends TSIDMapper, NameMapper, TimeStringMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(target = "commentId", source = "id")
    @Mapping(target = "postId", source = "post.id", qualifiedByName = "toTSIDString")
    @Mapping(target = "postTitle", source = "post.title")
    @Mapping(target = "createdById", source = "createdBy.id", qualifiedByName = "toTSIDString")
    @Mapping(target = "createdByName", source = "createdBy", qualifiedByName = "toUserName")
    @Mapping(target = "updatedById", source = "updatedBy.id", qualifiedByName = "toTSIDString")
    @Mapping(target = "updatedByName", source = "updatedBy", qualifiedByName = "toUserName")
    @Mapping(target = "createdOn", source = "createdOn", qualifiedByName = "toStringWithFormat")
    @Mapping(target = "updatedOn", source = "updatedOn", qualifiedByName = "toStringWithFormat")
    CommentResponse toCommentResponse(Comment comment);
}
