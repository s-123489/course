package com.zjsu.syt.course.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Course {
    private String id;
    private String code;
    private String title;
    private Instructor instructor;
    private ScheduleSlot schedule;
    private int capacity;
    private AtomicInteger enrolled;

    public Course() {
        this.enrolled = new AtomicInteger(0);
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Instructor getInstructor() { return instructor; }
    public void setInstructor(Instructor instructor) { this.instructor = instructor; }

    public ScheduleSlot getSchedule() { return schedule; }
    public void setSchedule(ScheduleSlot schedule) { this.schedule = schedule; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public int getEnrolled() { return enrolled.get(); }
    public void setEnrolled(int enrolled) { this.enrolled.set(enrolled); }
    public int incrementEnrolled() { return enrolled.incrementAndGet(); }
    public int decrementEnrolled() { return enrolled.decrementAndGet(); }
}