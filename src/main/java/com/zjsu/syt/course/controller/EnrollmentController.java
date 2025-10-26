package com.zjsu.syt.course.controller;

import com.zjsu.syt.course.dto.ApiResponse;
import com.zjsu.syt.course.model.Enrollment;
import com.zjsu.syt.course.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Enrollment>>> getAllEnrollments() {
        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
        ApiResponse<List<Enrollment>> response = new ApiResponse<>(
                200, "Success", enrollments
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Enrollment>> getEnrollmentById(@PathVariable String id) {
        Enrollment enrollment = enrollmentService.getEnrollmentById(id);
        ApiResponse<Enrollment> response = new ApiResponse<>(
                200, "Success", enrollment
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<Enrollment>>> getEnrollmentsByCourseId(
            @PathVariable String courseId) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourseId(courseId);
        ApiResponse<List<Enrollment>> response = new ApiResponse<>(
                200, "Success", enrollments
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<Enrollment>>> getEnrollmentsByStudentId(
            @PathVariable String studentId) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudentId(studentId);
        ApiResponse<List<Enrollment>> response = new ApiResponse<>(
                200, "Success", enrollments
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Enrollment>> enrollStudent(@RequestBody Map<String, String> request) {
        String courseId = request.get("courseId");
        String studentId = request.get("studentId");

        if (courseId == null || studentId == null) {
            throw new IllegalArgumentException("courseId 和 studentId 不能为空");
        }

        Enrollment enrollment = enrollmentService.enrollStudent(courseId, studentId);
        ApiResponse<Enrollment> response = new ApiResponse<>(
                201, "选课成功", enrollment
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> cancelEnrollment(@PathVariable String id) {
        enrollmentService.cancelEnrollment(id);
        ApiResponse<Void> response = new ApiResponse<>(
                200, "取消选课成功", null
        );
        return ResponseEntity.ok(response);
    }
}