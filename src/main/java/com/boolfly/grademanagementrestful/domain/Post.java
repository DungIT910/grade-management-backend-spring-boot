package com.boolfly.grademanagementrestful.domain;

import com.boolfly.grademanagementrestful.domain.base.AbstractAuditEntity;
import com.boolfly.grademanagementrestful.domain.model.post.PostStatus;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post extends AbstractAuditEntity {
    @Id
    private Long id;
    private String title;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    private Forum forum;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @Enumerated(EnumType.STRING)
    private PostStatus status;
}
