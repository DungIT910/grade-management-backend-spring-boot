package com.boolfly.GradeManagementRestful.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table
@Setter
@Getter
public class Maingrade {
    @Id
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private User student;
    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;
    private BigDecimal midtermGrade;
    private BigDecimal finalGrade;
}