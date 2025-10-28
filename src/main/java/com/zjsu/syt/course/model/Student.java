package com.zjsu.syt.course.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "students", uniqueConstraints = {
        @UniqueConstraint(columnNames = "studentId"),
        @UniqueConstraint(columnNames = "email")
})
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "student_id", nullable = false, unique = true, length = 20)
    private String studentId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String major;

    @Column(nullable = false)
    private Integer grade;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}