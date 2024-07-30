package com.boolfly.grademanagementrestful.api.dto.general;

import lombok.Getter;

import java.util.List;

@Getter
public class BatchRequest<T> {
    private List<T> batch;
}

