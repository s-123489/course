package com.zjsu.syt.course.service;

import com.zjsu.syt.course.model.Course;
import com.zjsu.syt.course.model.Enrollment;
import com.zjsu.syt.course.model.Student;
import com.zjsu.syt.course.repository.CourseRepository;
import com.zjsu.syt.course.repository.EnrollmentRepository;
import com.zjsu.syt.course.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    public Enrollment getEnrollmentById(String id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("选课记录不存在: " + id));
    }

    public List<Enrollment> getEnrollmentsByCourseId(String courseId) {
        // 验证课程是否存在
        if (!courseRepository.existsById(courseId)) {
            throw new NoSuchElementException("课程不存在: " + courseId);
        }
        return enrollmentRepository.findByCourseId(courseId);
    }

    public List<Enrollment> getEnrollmentsByStudentId(String studentId) {
        // 验证学生是否存在（通过学号）
        if (!studentRepository.existsByStudentId(studentId)) {
            throw new NoSuchElementException("学生不存在: " + studentId);
        }
        return enrollmentRepository.findByStudentId(studentId);
    }

    public Enrollment enrollStudent(String courseId, String studentId) {
        // 验证课程是否存在
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NoSuchElementException("课程不存在: " + courseId));

        // 验证学生是否存在（通过学号）
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new NoSuchElementException("学生不存在: " + studentId));

        // 检查是否已经选过该课程
        if (enrollmentRepository.existsByCourseIdAndStudentId(courseId, studentId)) {
            throw new IllegalArgumentException("学生已选过该课程");
        }

        // 检查课程容量
        int currentEnrollment = enrollmentRepository.countByCourseId(courseId);
        if (currentEnrollment >= course.getCapacity()) {
            throw new IllegalArgumentException("课程容量已满");
        }

        // 创建选课记录
        Enrollment enrollment = new Enrollment();
        enrollment.setCourseId(courseId);
        enrollment.setStudentId(studentId);
        enrollment.setEnrolledAt(LocalDateTime.now());

        // 保存选课记录
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        // 更新课程的已选人数
        course.incrementEnrolled();
        courseRepository.save(course);

        return savedEnrollment;
    }

    public void cancelEnrollment(String id) {
        Enrollment enrollment = getEnrollmentById(id);

        // 获取课程并更新已选人数
        Course course = courseRepository.findById(enrollment.getCourseId())
                .orElseThrow(() -> new NoSuchElementException("课程不存在: " + enrollment.getCourseId()));

        // 删除选课记录
        enrollmentRepository.deleteById(id);

        // 更新课程的已选人数
        course.decrementEnrolled();
        courseRepository.save(course);
    }

    public void cancelEnrollmentByCourseAndStudent(String courseId, String studentId) {
        Enrollment enrollment = enrollmentRepository.findByCourseIdAndStudentId(courseId, studentId)
                .orElseThrow(() -> new NoSuchElementException("选课记录不存在"));

        cancelEnrollment(enrollment.getId());
    }
}