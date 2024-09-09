package com.haohaodayouxi.demo.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

/**
 * 国际化配置
 *
 * @author TONE
 * @date 2024/9/9
 */
@Slf4j
@Configuration
public class I18nConfig {

    @Bean
    public LocaleResolver localeResolver() {
        return new I18nLocaleResolver();
    }

    /**
     * 自定义语言解析器
     */
    public static class I18nLocaleResolver implements LocaleResolver {

        @Override
        public Locale resolveLocale(HttpServletRequest request) {
            // 根据请求头Content-Language 获取语言信息
            String language = request.getHeader(HttpHeaders.CONTENT_LANGUAGE);
            Locale locale = request.getLocale();
            if (ObjectUtils.isNotEmpty(language)) {
                // 语言信息格式为：zh_CN(中文)、en_US(英文)
                String[] split = language.split("_");
                locale = Locale.of(split[0], split[1]);
            }
            return locale;
        }

        @Override
        public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {

        }
    }

}
