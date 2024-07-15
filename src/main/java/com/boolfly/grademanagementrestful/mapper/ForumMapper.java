package com.boolfly.grademanagementrestful.mapper;

import com.boolfly.grademanagementrestful.api.dto.forum.ForumResponse;
import com.boolfly.grademanagementrestful.domain.Course;
import com.boolfly.grademanagementrestful.domain.Forum;
import io.hypersistence.tsid.TSID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ForumMapper {
    ForumMapper INSTANCE = Mappers.getMapper(ForumMapper.class);

    @Named("toForumId")
    static String toForumId(Long id) {
        TSID tsid = TSID.from(id);
        return tsid.toString();
    }

    @Named("toCourseId")
    static String toCourseId(Course course) {
        return TSID.from(course.getId()).toString();
    }

    @Named("toCourseName")
    static String toCourseName(Course course) {
        return course.getName();
    }

    @Mapping(target = "forumId", source = "id", qualifiedByName = "toForumId")
    @Mapping(target = "courseId", source = "course", qualifiedByName = "toCourseId")
    @Mapping(target = "courseName", source = "course", qualifiedByName = "toCourseName")
    ForumResponse toForumResponse(Forum forum);
}
