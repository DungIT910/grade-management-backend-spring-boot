package com.boolfly.grademanagementrestful.api.dto.general;


import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PageResponse<T> {
    private List<T> content;
}
