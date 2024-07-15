package com.boolfly.grademanagementrestful.domain;

import com.boolfly.grademanagementrestful.domain.model.forum.ForumStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Forum {
    @Id
    private Long id;
    private String name;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;
    @Enumerated(EnumType.STRING)
    private ForumStatus status;
}
