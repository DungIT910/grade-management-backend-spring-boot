package com.boolfly.grademanagementrestful.mapper;

import io.hypersistence.tsid.TSID;
import org.mapstruct.Named;

public interface TSIDMapper {
    @Named("toTSIDString")
    static String toTSIDString(Long id) {
        return TSID.from(id).toString();
    }
}
