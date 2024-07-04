package com.boolfly.GradeManagementRestful.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
public class Course {
    @Id
    private Long id;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    private User lecturer;
    @ManyToOne(fetch = FetchType.LAZY)
    private Subject subject;
}
