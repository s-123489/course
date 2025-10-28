package com.zjsu.syt.course.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "courses", uniqueConstraints = {
        @UniqueConstraint(columnNames = "code")
})
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 200)
    private String title;

    @Embedded
    private Instructor instructor;

    @Embedded
    private ScheduleSlot schedule;

    @Column(nullable = false)
    private Integer capacity = 0;

    @Column(nullable = false)
    private Integer enrolled = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    @PreUpdate
    public void validateCapacity() {
        if (enrolled > capacity) {
            throw new IllegalArgumentException("已选人数不能超过容量");
        }
    }
}