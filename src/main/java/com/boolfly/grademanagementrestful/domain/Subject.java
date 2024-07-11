package com.boolfly.grademanagementrestful.domain;


import com.boolfly.grademanagementrestful.domain.model.subject.SubjectStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subject {
    @Id
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private SubjectStatus status;
}
