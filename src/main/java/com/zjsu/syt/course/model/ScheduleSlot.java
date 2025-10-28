package com.zjsu.syt.course.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.time.LocalTime;

@Data
@Embeddable
public class ScheduleSlot {

    @Column(name = "day_of_week", length = 20)
    private String dayOfWeek;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "expected_attendance")
    private Integer expectedAttendance;
}