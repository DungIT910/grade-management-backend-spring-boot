package com.boolfly.grademanagementrestful.mapper;

import com.boolfly.grademanagementrestful.api.dto.post.PostResponse;
import com.boolfly.grademanagementrestful.domain.Post;
import com.boolfly.grademanagementrestful.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Mapper
public interface PostMapper extends TSIDMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Named("toUserName")
    static String toUserName(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }

    @Named("toStringWithFormat")
    static String toStringWithFormat(Instant instant) {
        if (instant == null) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        return formatter.format(instant);
    }

    @Mapping(target = "postId", source = "id", qualifiedByName = "toTSIDString")
    @Mapping(target = "forumId", source = "forum.id", qualifiedByName = "toTSIDString")
    @Mapping(target = "forumName", source = "forum.name")
    @Mapping(target = "userId", source = "user.id", qualifiedByName = "toTSIDString")
    @Mapping(target = "userName", source = "user", qualifiedByName = "toUserName")
    @Mapping(target = "createdOn", source = "createdOn", qualifiedByName = "toStringWithFormat")
    @Mapping(target = "updatedOn", source = "updatedOn", qualifiedByName = "toStringWithFormat")
    PostResponse toPostResponse(Post post);
}
