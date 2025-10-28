package com.zjsu.syt.course.service;

import com.zjsu.syt.course.model.Course;
import com.zjsu.syt.course.repository.CourseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(String id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("课程不存在: " + id));
    }

    public Course getCourseByCode(String code) {
        return courseRepository.findByCode(code)
                .orElseThrow(() -> new NoSuchElementException("课程不存在: " + code));
    }

    @Transactional
    public Course createCourse(Course course) {
        // 检查课程代码是否已存在
        if (courseRepository.existsByCode(course.getCode())) {
            throw new IllegalArgumentException("课程代码已存在: " + course.getCode());
        }

        // 设置默认值
        if (course.getEnrolled() == null) {
            course.setEnrolled(0);
        }
        if (course.getCapacity() == null) {
            course.setCapacity(0);
        }

        return courseRepository.save(course);
    }

    @Transactional
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

    @Transactional
    public void deleteCourse(String id) {
        Course course = getCourseById(id);

        // 检查是否有选课记录
        // 这里需要在 EnrollmentService 中添加检查逻辑

        courseRepository.deleteById(id);
    }

    public List<Course> getAvailableCourses() {
        return courseRepository.findAvailableCourses();
    }

    public List<Course> searchCoursesByTitle(String keyword) {
        return courseRepository.findByTitleContaining(keyword);
    }

    public boolean existsById(String id) {
        return courseRepository.existsById(id);
    }
}