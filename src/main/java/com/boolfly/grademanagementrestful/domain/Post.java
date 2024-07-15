package com.boolfly.grademanagementrestful.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;


@Entity
@Table
@Setter
@Getter
public class Post {
    @Id
    private Long id;
    private String title;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    private Forum forum;
    @CreatedDate
    private Instant createdOn;
    @LastModifiedDate
    private Instant updatedOn;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
