package com.zjsu.syt.course.controller;

import com.zjsu.syt.course.dto.ApiResponse;
import com.zjsu.syt.course.model.Student;
import com.zjsu.syt.course.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Student>>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        ApiResponse<List<Student>> response = new ApiResponse<>(
                200, "Success", students
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Student>> getStudentById(@PathVariable String id) {
        Student student = studentService.getStudentById(id);
        ApiResponse<Student> response = new ApiResponse<>(
                200, "Success", student
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Student>> createStudent(@RequestBody Student student) {
        Student createdStudent = studentService.createStudent(student);
        ApiResponse<Student> response = new ApiResponse<>(
                201, "学生创建成功", createdStudent
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Student>> updateStudent(
            @PathVariable String id,
            @RequestBody Student student) {
        Student updatedStudent = studentService.updateStudent(id, student);
        ApiResponse<Student> response = new ApiResponse<>(
                200, "学生信息更新成功", updatedStudent
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable String id) {
        studentService.deleteStudent(id);
        ApiResponse<Void> response = new ApiResponse<>(
                200, "学生删除成功", null
        );
        return ResponseEntity.ok(response);
    }
}