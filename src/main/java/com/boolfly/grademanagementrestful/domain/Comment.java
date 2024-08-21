package com.boolfly.grademanagementrestful.domain;

import com.boolfly.grademanagementrestful.domain.base.AbstractAuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends AbstractAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @Builder.Default
    @OneToMany(mappedBy = "ancestor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CommentRelation> descendantRelations = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "descendant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CommentRelation> ancestorRelations = new HashSet<>();
}
