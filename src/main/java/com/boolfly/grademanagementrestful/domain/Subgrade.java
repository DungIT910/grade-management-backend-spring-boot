package com.boolfly.grademanagementrestful.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Subgrade {
    @Id
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Subcol subcol;
    @ManyToOne(fetch = FetchType.LAZY)
    private User student;
    private Double grade;
}
