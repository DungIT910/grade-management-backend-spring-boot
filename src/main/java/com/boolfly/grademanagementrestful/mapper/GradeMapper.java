package com.boolfly.grademanagementrestful.mapper;

import com.boolfly.grademanagementrestful.api.dto.grade.GradeResponse;
import com.boolfly.grademanagementrestful.api.dto.grade.MaingradeDetailsResponse;
import com.boolfly.grademanagementrestful.api.dto.grade.SubgradeDetailsResponse;
import com.boolfly.grademanagementrestful.domain.Maingrade;
import com.boolfly.grademanagementrestful.domain.Subgrade;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GradeMapper extends TSIDMapper, NameMapper {
    GradeMapper INSTANCE = Mappers.getMapper(GradeMapper.class);

    @Mapping(target = "maingradeId", source = "id", qualifiedByName = "toTSIDString")
    @Mapping(target = "studentId", source = "student.id", qualifiedByName = "toTSIDString")
    @Mapping(target = "studentName", source = "student", qualifiedByName = "toUserName")
    @Mapping(target = "courseId", source = "course.id", qualifiedByName = "toTSIDString")
    @Mapping(target = "courseName", source = "course.name")
    @Mapping(target = "subgradeList", ignore = true)
    GradeResponse toGradeResponse(Maingrade maingrade);

    @Mapping(target = "maingradeId", source = "id", qualifiedByName = "toTSIDString")
    @Mapping(target = "studentId", source = "student.id", qualifiedByName = "toTSIDString")
    @Mapping(target = "studentName", source = "student", qualifiedByName = "toUserName")
    @Mapping(target = "courseId", source = "course.id", qualifiedByName = "toTSIDString")
    @Mapping(target = "courseName", source = "course.name")
    MaingradeDetailsResponse toMaingradeDetailsResponse(Maingrade maingrade);

    @Mapping(target = "subgradeId", source = "id", qualifiedByName = "toTSIDString")
    @Mapping(target = "subcolId", source = "subcol.id", qualifiedByName = "toTSIDString")
    @Mapping(target = "subcolName", source = "subcol.name")
    @Mapping(target = "studentId", source = "student.id", qualifiedByName = "toTSIDString")
    @Mapping(target = "studentName", source = "student", qualifiedByName = "toUserName")
    @Mapping(target = "courseId", source = "subcol.course.id", qualifiedByName = "toTSIDString")
    @Mapping(target = "courseName", source = "subcol.course.name")
    SubgradeDetailsResponse toSubgradeDetailsResponse(Subgrade subgrade);
}
