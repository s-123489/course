package com.zjsu.syt.course.service;

import com.zjsu.syt.course.model.Student;
import com.zjsu.syt.course.repository.EnrollmentRepository;
import com.zjsu.syt.course.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;  // 添加这行导入
import java.util.regex.Pattern;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(String id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("学生不存在: " + id));
    }

    public Student createStudent(Student student) {
        // 验证必填字段
        validateStudentFields(student);

        // 检查学号是否唯一
        if (studentRepository.existsByStudentId(student.getStudentId())) {
            throw new IllegalArgumentException("学号已存在: " + student.getStudentId());
        }

        // 验证邮箱格式
        if (!isValidEmail(student.getEmail())) {
            throw new IllegalArgumentException("邮箱格式无效: " + student.getEmail());
        }

        // 设置系统生成的字段
        student.setCreatedAt(LocalDateTime.now());

        return studentRepository.save(student);
    }

    public Student updateStudent(String id, Student student) {
        Student existingStudent = getStudentById(id);

        // 验证必填字段
        validateStudentFields(student);

        // 如果学号被修改，检查是否与其他学生冲突
        if (!existingStudent.getStudentId().equals(student.getStudentId())) {
            if (studentRepository.existsByStudentId(student.getStudentId())) {
                throw new IllegalArgumentException("学号已存在: " + student.getStudentId());
            }
        }

        // 验证邮箱格式
        if (!isValidEmail(student.getEmail())) {
            throw new IllegalArgumentException("邮箱格式无效: " + student.getEmail());
        }

        // 更新允许修改的字段
        existingStudent.setStudentId(student.getStudentId());
        existingStudent.setName(student.getName());
        existingStudent.setMajor(student.getMajor());
        existingStudent.setGrade(student.getGrade());
        existingStudent.setEmail(student.getEmail());

        return studentRepository.save(existingStudent);
    }

    public void deleteStudent(String id) {
        Student student = getStudentById(id);

        // 检查是否有选课记录
        int enrollmentCount = enrollmentRepository.countByStudentId(student.getStudentId());
        if (enrollmentCount > 0) {
            throw new IllegalStateException("无法删除：该学生存在选课记录");
        }

        studentRepository.deleteById(id);
    }

    private void validateStudentFields(Student student) {
        if (student.getStudentId() == null || student.getStudentId().trim().isEmpty()) {
            throw new IllegalArgumentException("学号不能为空");
        }
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("姓名不能为空");
        }
        if (student.getMajor() == null || student.getMajor().trim().isEmpty()) {
            throw new IllegalArgumentException("专业不能为空");
        }
        if (student.getGrade() == null) {
            throw new IllegalArgumentException("入学年份不能为空");
        }
        if (student.getEmail() == null || student.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("邮箱不能为空");
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public boolean existsByStudentId(String studentId) {
        return studentRepository.existsByStudentId(studentId);
    }

    public Optional<Student> findByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId);
    }
}