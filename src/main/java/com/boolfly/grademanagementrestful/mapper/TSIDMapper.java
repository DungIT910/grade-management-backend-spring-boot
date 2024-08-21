package com.boolfly.grademanagementrestful.mapper;

import io.hypersistence.tsid.TSID;
import org.mapstruct.Named;

public interface TSIDMapper {
    @Named("toTSIDString")
    static String toTSIDString(Long id) {
        if (id == null) {
            return null;
        }
        return TSID.from(id).toString();
    }
}
