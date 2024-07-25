package com.boolfly.grademanagementrestful.domain;

import com.boolfly.grademanagementrestful.domain.model.maingrade.MaingradeStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Maingrade {
    @Id
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private User student;
    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;
    private Double midtermGrade;
    private Double finalGrade;
    @Enumerated(EnumType.STRING)
    private MaingradeStatus status;
}
