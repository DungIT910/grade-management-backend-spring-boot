package com.boolfly.grademanagementrestful.api.dto.general;

import lombok.NoArgsConstructor;

import java.util.Collection;

@NoArgsConstructor
public class BatchResponse<T> extends CollectionResponse<T> {
    public void setBatch(Collection<T> content) {
        setContent(content);
    }
}
