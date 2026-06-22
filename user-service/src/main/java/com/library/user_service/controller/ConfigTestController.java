package com.library.user_service.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class ConfigTestController {

    @Value("${spring.datasource.username:Not Found}")
    private String dbUsername;

    @GetMapping("/test-config")
    public String testConfig() {
        return "Current DB Username from Config Server: " + dbUsername;
    }
}