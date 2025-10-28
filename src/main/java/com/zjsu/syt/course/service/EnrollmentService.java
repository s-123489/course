package com.zjsu.syt.course.service;

import com.zjsu.syt.course.model.Course;
import com.zjsu.syt.course.model.Enrollment;
import com.zjsu.syt.course.model.EnrollmentStatus;
import com.zjsu.syt.course.model.Student;
import com.zjsu.syt.course.repository.CourseRepository;
import com.zjsu.syt.course.repository.EnrollmentRepository;
import com.zjsu.syt.course.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

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
        // 验证学生是否存在
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new NoSuchElementException("学生不存在: " + studentId));
        return enrollmentRepository.findByStudentId(student.getStudentId());
    }

    public List<Enrollment> getActiveEnrollmentsByCourseId(String courseId) {
        return enrollmentRepository.findByCourseIdAndStatus(courseId, EnrollmentStatus.ACTIVE);
    }

    public List<Enrollment> getActiveEnrollmentsByStudentId(String studentId) {
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new NoSuchElementException("学生不存在: " + studentId));
        return enrollmentRepository.findByStudentIdAndStatus(student.getStudentId(), EnrollmentStatus.ACTIVE);
    }

    @Transactional
    public Enrollment enrollStudent(String courseId, String studentId) {
        // 验证课程是否存在
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NoSuchElementException("课程不存在: " + courseId));

        // 验证学生是否存在
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new NoSuchElementException("学生不存在: " + studentId));

        // 检查是否已经选过该课程
        if (enrollmentRepository.existsByCourseIdAndStudentIdAndStatus(courseId, student.getStudentId(), EnrollmentStatus.ACTIVE)) {
            throw new IllegalArgumentException("学生已选过该课程");
        }

        // 检查课程容量
        int currentEnrollment = enrollmentRepository.countActiveEnrollmentsByCourseId(courseId);
        if (currentEnrollment >= course.getCapacity()) {
            throw new IllegalArgumentException("课程容量已满");
        }

        // 创建选课记录
        Enrollment enrollment = new Enrollment();
        enrollment.setCourseId(courseId);
        enrollment.setStudentId(student.getStudentId());
        enrollment.setStatus(EnrollmentStatus.ACTIVE);

        // 保存选课记录
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        // 更新课程的已选人数
        course.setEnrolled(currentEnrollment + 1);
        courseRepository.save(course);

        return savedEnrollment;
    }

    @Transactional
    public void cancelEnrollment(String id) {
        Enrollment enrollment = getEnrollmentById(id);

        // 获取课程并更新已选人数
        Course course = courseRepository.findById(enrollment.getCourseId())
                .orElseThrow(() -> new NoSuchElementException("课程不存在: " + enrollment.getCourseId()));

        // 更新选课状态为已退课
        enrollment.setStatus(EnrollmentStatus.DROPPED);
        enrollmentRepository.save(enrollment);

        // 更新课程的已选人数
        int currentEnrollment = enrollmentRepository.countActiveEnrollmentsByCourseId(enrollment.getCourseId());
        course.setEnrolled(currentEnrollment);
        courseRepository.save(course);
    }

    @Transactional
    public void cancelEnrollmentByCourseAndStudent(String courseId, String studentId) {
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new NoSuchElementException("学生不存在: " + studentId));

        Enrollment enrollment = enrollmentRepository.findByCourseIdAndStudentId(courseId, student.getStudentId())
                .orElseThrow(() -> new NoSuchElementException("选课记录不存在"));

        cancelEnrollment(enrollment.getId());
    }

    @Transactional
    public void completeEnrollment(String id) {
        Enrollment enrollment = getEnrollmentById(id);
        enrollment.setStatus(EnrollmentStatus.COMPLETED);
        enrollmentRepository.save(enrollment);
    }

    public int getActiveEnrollmentCountByCourse(String courseId) {
        return enrollmentRepository.countActiveEnrollmentsByCourseId(courseId);
    }

    public int getActiveEnrollmentCountByStudent(String studentId) {
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new NoSuchElementException("学生不存在: " + studentId));
        return enrollmentRepository.countActiveEnrollmentsByStudentId(student.getStudentId());
    }
}