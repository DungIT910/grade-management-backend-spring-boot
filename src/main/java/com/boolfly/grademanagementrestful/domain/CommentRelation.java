package com.boolfly.grademanagementrestful.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ancestor_id")
    private Comment ancestor;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "descendant_id")
    private Comment descendant;
    private Integer depth;
}
