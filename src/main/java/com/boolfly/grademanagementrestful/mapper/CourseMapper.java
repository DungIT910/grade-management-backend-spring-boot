package com.boolfly.grademanagementrestful.mapper;

import com.boolfly.grademanagementrestful.api.dto.course.CourseResponse;
import com.boolfly.grademanagementrestful.domain.Course;
import com.boolfly.grademanagementrestful.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Mapper
public interface CourseMapper extends TSIDMapper {
    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

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

    @Mapping(target = "courseId", source = "id", qualifiedByName = "toTSIDString")
    @Mapping(target = "subjectId", source = "subject.id", qualifiedByName = "toTSIDString")
    @Mapping(target = "lecturerId", source = "lecturer.id", qualifiedByName = "toTSIDString")
    @Mapping(target = "subjectName", source = "subject.name")
    @Mapping(target = "lecturerName", source = "lecturer", qualifiedByName = "toLecturerName")
    @Mapping(target = "startTime", source = "startTime", qualifiedByName = "toLocalDate")
    @Mapping(target = "endTime", source = "endTime", qualifiedByName = "toLocalDate")
    CourseResponse toCourseResponse(Course course);
}
