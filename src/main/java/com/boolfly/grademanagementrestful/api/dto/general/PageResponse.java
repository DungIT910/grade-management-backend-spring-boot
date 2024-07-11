package com.boolfly.grademanagementrestful.api.dto.general;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> {
    private List<T> content;
}
