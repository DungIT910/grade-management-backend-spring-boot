package com.boolfly.grademanagementrestful.builder.entitypathprovider.base;

import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.List;

public interface CommonEntityPathProvider<T> {
    BooleanExpression nameLikeIgnoreCase(String name);

    BooleanExpression idEquals(Long id);

    BooleanExpression statusIn(List<T> status);
}
