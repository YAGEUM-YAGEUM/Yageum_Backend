package com.yageum.fintech.domain.project.infrastructure;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "project_user",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"project_id", "user_id"})} )
public class ProjectUser {
    @Id
    @Column(name = "project_user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectUserId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

}
