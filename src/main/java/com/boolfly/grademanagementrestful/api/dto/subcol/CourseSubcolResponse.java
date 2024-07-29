package com.boolfly.grademanagementrestful.api.dto.subcol;

import com.boolfly.grademanagementrestful.api.dto.general.CollectionResponse;

import java.util.Collection;

public class CourseSubcolResponse extends CollectionResponse<SubcolResponse> {
    public void setSubcols(Collection<SubcolResponse> content) {
        setContent(content);
    }
}
