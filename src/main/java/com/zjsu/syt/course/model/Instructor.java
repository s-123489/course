package com.zjsu.syt.course.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Instructor {

    @Column(name = "instructor_id", length = 50)
    private String id;

    @Column(name = "instructor_name", length = 100)
    private String name;

    @Column(name = "instructor_email", length = 100)
    private String email;
}