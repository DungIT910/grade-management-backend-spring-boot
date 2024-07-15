package com.boolfly.grademanagementrestful.domain;

import com.boolfly.grademanagementrestful.domain.model.course.CourseStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @Id
    private Long id;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    private User lecturer;
    @ManyToOne(fetch = FetchType.LAZY)
    private Subject subject;
    private Instant startTime;
    private Instant endTime;
    private Integer minQuantity;
    @Enumerated(EnumType.STRING)
    private CourseStatus status;
}
