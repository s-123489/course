package com.zjsu.syt.course.controller;

import com.zjsu.syt.course.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StatusController {

    private final JdbcTemplate jdbcTemplate;

    @GetMapping("/status")
    public ApiResponse<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("service", "校园选课系统");
        status.put("version", "1.1.0");
        status.put("status", "运行正常");
        status.put("timestamp", LocalDateTime.now());
        status.put("author", "syt");
        status.put("persistence", "Database (JPA)");

        // 添加数据库连接状态
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            status.put("database", "Connected");
        } catch (Exception e) {
            status.put("database", "Disconnected");
            status.put("error", e.getMessage());
        }

        return new ApiResponse<>(200, "服务状态正常", status);
    }

    // ... 其他方法保持不变
}