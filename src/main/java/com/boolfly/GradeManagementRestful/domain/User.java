package com.boolfly.GradeManagementRestful.domain;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @ManyToOne(fetch = FetchType.LAZY)
    private Role role;
    private String avatar;
    private Boolean active;
}
