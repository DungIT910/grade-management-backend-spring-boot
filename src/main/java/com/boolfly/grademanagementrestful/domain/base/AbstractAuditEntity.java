package com.boolfly.grademanagementrestful.domain.base;

import com.boolfly.grademanagementrestful.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

/**
 * Abstract base class for entities to automatically handle audit fields.
 * <p>
 * This class is annotated with {@link MappedSuperclass} to indicate that it is a base class for JPA entities,
 * and with {@link EntityListeners} to enable auditing using {@link AuditingEntityListener}.
 * The {@link CreatedDate} and {@link LastModifiedDate} annotations are used to automatically populate the audit fields.
 *
 * @author Bao Le
 * @see MappedSuperclass
 * @see EntityListeners
 * @see AuditingEntityListener
 * @see CreatedDate
 * @see LastModifiedDate
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditEntity {
    @CreatedDate
    private Instant createdOn;

    @LastModifiedDate
    private Instant updatedOn;

    @CreatedBy
    @JoinColumn(name = "created_by")
    @ManyToOne(fetch = FetchType.EAGER)
    private User createdBy;

    @LastModifiedBy
    @JoinColumn(name = "updated_by")
    @ManyToOne(fetch = FetchType.EAGER)
    private User updatedBy;
}
