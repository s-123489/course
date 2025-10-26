package com.zjsu.syt.course.controller;

import com.zjsu.syt.course.dto.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class StatusController {

    @GetMapping("/status")
    public ApiResponse<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("service", "校园选课系统");
        status.put("version", "1.0.0");
        status.put("status", "运行正常");
        status.put("timestamp", LocalDateTime.now());
        status.put("author", "syt");

        return new ApiResponse<>(200, "服务状态正常", status);
    }

    @GetMapping("/")
    public ApiResponse<Map<String, Object>> welcome() {
        Map<String, Object> info = new HashMap<>();
        info.put("message", "欢迎使用校园选课系统");
        info.put("author", "syt");
        info.put("version", "1.0.0");
        info.put("availableEndpoints", new String[]{
                "GET  /api/status - 服务状态",
                "GET  /api/courses - 查询所有课程",
                "POST /api/courses - 创建课程",
                "GET  /api/courses/{id} - 查询单个课程",
                "PUT  /api/courses/{id} - 更新课程",
                "DELETE /api/courses/{id} - 删除课程",
                "GET  /api/students - 查询所有学生",
                "POST /api/students - 创建学生",
                "GET  /api/students/{id} - 查询单个学生",
                "PUT  /api/students/{id} - 更新学生",
                "DELETE /api/students/{id} - 删除学生",
                "GET  /api/enrollments - 查询所有选课",
                "POST /api/enrollments - 学生选课",
                "DELETE /api/enrollments/{id} - 取消选课",
                "GET  /api/enrollments/course/{courseId} - 按课程查询选课",
                "GET  /api/enrollments/student/{studentId} - 按学生查询选课"
        });

        return new ApiResponse<>(200, "欢迎使用", info);
    }
}