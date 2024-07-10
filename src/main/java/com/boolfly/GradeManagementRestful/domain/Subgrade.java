package com.boolfly.GradeManagementRestful.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table
@Setter
@Getter
public class Subgrade {
    @Id
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Subcol subcol;
    @ManyToOne(fetch = FetchType.LAZY)
    private User student;
    private BigDecimal grade;
}
