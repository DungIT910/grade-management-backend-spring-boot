package com.boolfly.grademanagementrestful.api.dto.general;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
public class CollectionResponse<T> {
    private Collection<T> content;

    public CollectionResponse(Collection<T> content) {
        this.content = content;
    }
}
