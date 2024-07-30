package com.boolfly.grademanagementrestful.api.dto.general;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@NoArgsConstructor
@Getter
@Setter
public class PageResponse<T> extends CollectionResponse<T> {
    @Builder
    public PageResponse(Collection<T> content) {
        super(content);
    }
}
