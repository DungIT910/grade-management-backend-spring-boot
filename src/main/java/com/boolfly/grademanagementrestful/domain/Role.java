package com.boolfly.grademanagementrestful.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table
@Setter
@Getter
public class Role implements Serializable {
    private static final long serialVersionUID = 7211743806042385881L;
    @Id
    private Long id;
    private String name;
}
