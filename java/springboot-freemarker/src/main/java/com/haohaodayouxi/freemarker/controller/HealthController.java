package com.haohaodayouxi.freemarker.controller;

import com.haohaodayouxi.freemarker.utils.LayoutUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * HealthController
 *
 * @author TONE
 * @date 2024/8/24
 */
@Slf4j
@Controller // 不能使用@RestController，因为@RestController返回的是json格式
@RequestMapping("/health")
public class HealthController {
    /**
     * 项目名称
     */
    @Value("${spring.application.name:freemarker}")
    private String applicationName;

    /**
     * 单一页面
     *
     * @param model
     * @return
     */
    @GetMapping("/h-open/check")
    public String healthCheck(Model model) {
        log.info("healthCheck");
        model.addAttribute("applicationName", applicationName);
        return "healthCheck";
    }

    /**
     * 使用模板页面
     *
     * @param request
     * @return
     */
    @GetMapping("/h-open/check2")
    public String healthCheck2(HttpServletRequest request) {
        log.info("healthCheck2");
        request.setAttribute("applicationName", applicationName);
        return LayoutUtil.freeMarkerIndexResult("healthCheck2.ftl", request);
    }

}
