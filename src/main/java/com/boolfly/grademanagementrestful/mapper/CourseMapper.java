package com.boolfly.grademanagementrestful.mapper;

import com.boolfly.grademanagementrestful.api.dto.course.CourseResponse;
import com.boolfly.grademanagementrestful.domain.Course;
import com.boolfly.grademanagementrestful.domain.Subject;
import com.boolfly.grademanagementrestful.domain.User;
import io.hypersistence.tsid.TSID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

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

    @Named("toLecturerName")
    static String toLecturerName(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }

    @Named("toLocalDate")
    static LocalDate toLocalDate(Instant instant) {
        if (instant == null) {
            return null;
        }
        return LocalDate.ofInstant(instant, ZoneId.systemDefault());
    }

    @Mapping(target = "courseId", source = "id", qualifiedByName = "toCourseId")
    @Mapping(target = "subjectId", source = "subject", qualifiedByName = "toSubjectId")
    @Mapping(target = "lecturerId", source = "lecturer", qualifiedByName = "toLecturerId")
    @Mapping(target = "subjectName", source = "subject.name")
    @Mapping(target = "lecturerName", source = "lecturer", qualifiedByName = "toLecturerName")
    @Mapping(target = "startTime", source = "startTime", qualifiedByName = "toLocalDate")
    @Mapping(target = "endTime", source = "endTime", qualifiedByName = "toLocalDate")
    CourseResponse toCourseResponse(Course course);
}
