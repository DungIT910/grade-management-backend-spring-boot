package com.boolfly.grademanagementrestful.mapper;

import com.boolfly.grademanagementrestful.api.dto.grade.SubgradeResponse;
import com.boolfly.grademanagementrestful.repository.custom.model.PairSubgradeSubcol;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubgradeMapper extends TSIDMapper {
    SubgradeMapper INSTANCE = Mappers.getMapper(SubgradeMapper.class);

    @Mapping(target = "subgradeId", source = "subgrade.id", qualifiedByName = "toTSIDString")
    @Mapping(target = "subcolId", source = "subcol.id", qualifiedByName = "toTSIDString")
    @Mapping(target = "subcolName", source = "subcol.name")
    @Mapping(target = "grade", source = "subgrade.grade")
    SubgradeResponse toSubgradeResponse(PairSubgradeSubcol pairSubgradeSubcol);
}
