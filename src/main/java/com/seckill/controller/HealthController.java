package com.seckill.controller;

import com.seckill.common.ApiResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 健康检查：部署后先用它确认后端和数据库是否正常
 */
@RestController
@RequestMapping("/api/health")
public class HealthController {

    private final JdbcTemplate jdbc;

    public HealthController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @GetMapping
    public ApiResponse<Map<String, Object>> health() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("service", "seckill-backend");
        data.put("status", "UP");
        try {
            jdbc.queryForObject("select 1", Integer.class);
            data.put("database", "CONNECTED");
        } catch (Exception e) {
            data.put("database", "ERROR: " + e.getMessage());
        }
        return ApiResponse.ok(data);
    }
}
