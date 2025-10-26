package com.zjsu.syt.course.controller;

import com.zjsu.syt.course.dto.ApiResponse;
import com.zjsu.syt.course.model.Course;
import com.zjsu.syt.course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Course>>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        ApiResponse<List<Course>> response = new ApiResponse<>(
                200, "Success", courses
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Course>> getCourseById(@PathVariable String id) {
        Course course = courseService.getCourseById(id);
        ApiResponse<Course> response = new ApiResponse<>(
                200, "Success", course
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Course>> createCourse(@RequestBody Course course) {
        Course createdCourse = courseService.createCourse(course);
        ApiResponse<Course> response = new ApiResponse<>(
                201, "课程创建成功", createdCourse
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Course>> updateCourse(
            @PathVariable String id,
            @RequestBody Course course) {
        Course updatedCourse = courseService.updateCourse(id, course);
        ApiResponse<Course> response = new ApiResponse<>(
                200, "课程更新成功", updatedCourse
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable String id) {
        courseService.deleteCourse(id);
        ApiResponse<Void> response = new ApiResponse<>(
                200, "课程删除成功", null
        );
        return ResponseEntity.ok(response);
    }
}