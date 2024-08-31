package com.haohaodayouxi.auth.config;

import com.haohaodayouxi.auth.intercepter.*;
import com.haohaodayouxi.common.util.business.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebAuthConfig
 *
 * @author TONE
 * @date 2024/8/29
 */
@Slf4j
@Order(-1)
@Configuration
public class WebAuthConfig implements WebMvcConfigurer {

    /**
     * 拦截器注册
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(SpringContextHolder.getBean(InitParamInterceptor.class)).addPathPatterns("/**");
        registry.addInterceptor(SpringContextHolder.getBean(PathCheckInterceptor.class)).addPathPatterns("/**");
        registry.addInterceptor(SpringContextHolder.getBean(TokenCheckInterceptor.class)).addPathPatterns("/**");
        registry.addInterceptor(SpringContextHolder.getBean(UserCheckInterceptor.class)).addPathPatterns("/**");
        registry.addInterceptor(SpringContextHolder.getBean(ApiCheckInterceptor.class)).addPathPatterns("/**");
    }
}
