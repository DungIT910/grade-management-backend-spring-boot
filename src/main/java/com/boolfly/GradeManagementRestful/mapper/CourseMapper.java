package com.boolfly.GradeManagementRestful.mapper;

import com.boolfly.GradeManagementRestful.api.dto.course.CourseResponse;
import com.boolfly.GradeManagementRestful.domain.Course;
import com.boolfly.GradeManagementRestful.domain.Subject;
import com.boolfly.GradeManagementRestful.domain.User;
import io.hypersistence.tsid.TSID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CourseMapper {
    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    @Named("toCourseId")
    static String toCourseId(Long id) {
        TSID tsid = TSID.from(id);

        return tsid.toString();
    }

    @Named("toSubjectId")
    static String toSubjectId(Subject subject) {
        return TSID.from(subject.getId()).toString();
    }

    @Named("toLecturerId")
    static String toLecturerId(User user) {
        return TSID.from(user.getId()).toString();
    }

    @Mapping(target = "courseId", source = "id", qualifiedByName = "toCourseId")
    @Mapping(target = "subjectId", source = "subject", qualifiedByName = "toSubjectId")
    @Mapping(target = "lecturerId", source = "lecturer", qualifiedByName = "toLecturerId")
    CourseResponse toCourseResponse(Course course);
}
