package com.zjsu.syt.course.controller;

import com.zjsu.syt.course.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
public class DatabaseHealthController {

    private final JdbcTemplate jdbcTemplate;

    @GetMapping("/db")
    public ApiResponse<Map<String, Object>> checkDatabase() {
        Map<String, Object> healthInfo = new HashMap<>();

        try {
            // 测试数据库连接
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);

            healthInfo.put("status", "UP");
            healthInfo.put("database", "Connected");
            healthInfo.put("timestamp", java.time.LocalDateTime.now());

            return new ApiResponse<>(200, "数据库连接正常", healthInfo);

        } catch (Exception e) {
            healthInfo.put("status", "DOWN");
            healthInfo.put("database", "Disconnected");
            healthInfo.put("error", e.getMessage());
            healthInfo.put("timestamp", java.time.LocalDateTime.now());

            return new ApiResponse<>(503, "数据库连接异常", healthInfo);
        }
    }
}