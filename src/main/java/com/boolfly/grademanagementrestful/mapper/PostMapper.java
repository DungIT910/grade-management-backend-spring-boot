package com.boolfly.grademanagementrestful.mapper;

import com.boolfly.grademanagementrestful.api.dto.post.PostResponse;
import com.boolfly.grademanagementrestful.domain.Forum;
import com.boolfly.grademanagementrestful.domain.Post;
import com.boolfly.grademanagementrestful.domain.User;
import io.hypersistence.tsid.TSID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Named("toPostId")
    static String toPostId(Long id) {
        return TSID.from(id).toString();
    }

    @Named("toForumId")
    static String toForumId(Forum forum) {
        return TSID.from(forum.getId()).toString();
    }

    @Named("toForumName")
    static String toForumName(Forum forum) {
        return forum.getName();
    }

    @Named("toUserId")
    static String toUserId(User user) {
        return TSID.from(user.getId()).toString();
    }

    @Named("toUserName")
    static String toUserName(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }

    @Named("toLocalDateTime")
    static LocalDateTime toLocalDateTime(Instant instant) {
        if (instant == null) {
            return null;
        }
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    @Mapping(target = "postId", source = "id", qualifiedByName = "toPostId")
    @Mapping(target = "forumId", source = "forum", qualifiedByName = "toForumId")
    @Mapping(target = "forumName", source = "forum", qualifiedByName = "toForumName")
    @Mapping(target = "userId", source = "user", qualifiedByName = "toUserId")
    @Mapping(target = "userName", source = "user", qualifiedByName = "toUserName")
    @Mapping(target = "createdOn", source = "createdOn", qualifiedByName = "toLocalDateTime")
    @Mapping(target = "updatedOn", source = "updatedOn", qualifiedByName = "toLocalDateTime")
    PostResponse toPostResponse(Post post);
}
