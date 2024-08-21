package com.boolfly.grademanagementrestful.mapper;

import org.mapstruct.Named;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public interface TimeStringMapper {
    @Named("toStringWithFormat")
    static String toStringWithFormat(Instant instant) {
        if (instant == null) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        return formatter.format(instant);
    }
}
