package com.boolfly.grademanagementrestful.mapper;

import com.boolfly.grademanagementrestful.domain.User;
import org.mapstruct.Named;

public interface NameMapper {
    @Named("toUserName")
    static String toUserName(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }
}