package com.zjsu.syt.course.service;

import com.zjsu.syt.course.model.Student;
import com.zjsu.syt.course.repository.EnrollmentRepository;
import com.zjsu.syt.course.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(String id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("学生不存在: " + id));
    }

    @Transactional
    public Student createStudent(Student student) {
        // 验证必填字段
        validateStudentFields(student);

        // 检查学号是否唯一
        if (studentRepository.existsByStudentId(student.getStudentId())) {
            throw new IllegalArgumentException("学号已存在: " + student.getStudentId());
        }

        // 检查邮箱是否唯一
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new IllegalArgumentException("邮箱已存在: " + student.getEmail());
        }

        // 验证邮箱格式
        if (!isValidEmail(student.getEmail())) {
            throw new IllegalArgumentException("邮箱格式无效: " + student.getEmail());
        }

        return studentRepository.save(student);
    }

    @Transactional
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

        // 如果邮箱被修改，检查是否与其他学生冲突
        if (!existingStudent.getEmail().equals(student.getEmail())) {
            if (studentRepository.existsByEmail(student.getEmail())) {
                throw new IllegalArgumentException("邮箱已存在: " + student.getEmail());
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

    @Transactional
    public void deleteStudent(String id) {
        Student student = getStudentById(id);

        // 检查是否有选课记录
        int enrollmentCount = enrollmentRepository.countActiveEnrollmentsByStudentId(student.getStudentId());
        if (enrollmentCount > 0) {
            throw new IllegalStateException("无法删除：该学生存在活跃的选课记录");
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

    public List<Student> getStudentsByMajor(String major) {
        return studentRepository.findByMajor(major);
    }

    public List<Student> getStudentsByGrade(Integer grade) {
        return studentRepository.findByGrade(grade);
    }
}