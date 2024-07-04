package com.boolfly.GradeManagementRestful.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Setter
@Getter
public class Subject {
    @Id
    private Long id;
    private String name;
}
