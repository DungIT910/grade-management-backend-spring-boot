package com.boolfly.grademanagementrestful.api.dto.general;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BatchResponse<T> {
    List<T> batch;
}
