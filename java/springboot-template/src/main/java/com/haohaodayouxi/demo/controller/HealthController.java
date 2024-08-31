package com.haohaodayouxi.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * HealthController
 *
 * @author TONE
 * @date 2024/8/24
 */
@Slf4j
@RestController
@RequestMapping("/health")
public class HealthController {
    /**
     * 项目名称
     */
    @Value("${spring.application.name:demo}")
    private String applicationName;


    @GetMapping("/h-open/check")
    public String healthCheck() {
        log.info("healthCheck");
        return String.format(applicationName + " is health! %s", LocalDateTime.now().toString());
    }

}
