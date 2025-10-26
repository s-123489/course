package com.zjsu.syt.course.service;

import com.zjsu.syt.course.model.Course;
import com.zjsu.syt.course.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(String id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("课程不存在: " + id));
    }

    public Course createCourse(Course course) {
        // 检查课程代码是否已存在
        courseRepository.findByCode(course.getCode())
                .ifPresent(existingCourse -> {
                    throw new IllegalArgumentException("课程代码已存在: " + course.getCode());
                });

        return courseRepository.save(course);
    }

    public Course updateCourse(String id, Course course) {
        Course existingCourse = getCourseById(id);

        // 如果课程代码被修改，检查是否与其他课程冲突
        if (!existingCourse.getCode().equals(course.getCode())) {
            courseRepository.findByCode(course.getCode())
                    .ifPresent(conflictCourse -> {
                        if (!conflictCourse.getId().equals(id)) {
                            throw new IllegalArgumentException("课程代码已存在: " + course.getCode());
                        }
                    });
        }

        // 更新字段
        existingCourse.setCode(course.getCode());
        existingCourse.setTitle(course.getTitle());
        existingCourse.setInstructor(course.getInstructor());
        existingCourse.setSchedule(course.getSchedule());
        existingCourse.setCapacity(course.getCapacity());

        return courseRepository.save(existingCourse);
    }

    public void deleteCourse(String id) {
        if (!courseRepository.existsById(id)) {
            throw new NoSuchElementException("课程不存在: " + id);
        }
        courseRepository.deleteById(id);
    }

    public boolean existsById(String id) {
        return courseRepository.existsById(id);
    }
}