package com.boolfly.grademanagementrestful.domain;

import com.boolfly.grademanagementrestful.domain.model.subcol.SubcolStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Subcol {
    @Id
    private Long id;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;
    @Enumerated(EnumType.STRING)
    private SubcolStatus status;
}
