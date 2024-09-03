package com.haohaodayouxi.auth2.filter;

import com.haohaodayouxi.auth2.config.SysAuthProperties;
import com.haohaodayouxi.common.core.constants.CurrentParam;
import com.haohaodayouxi.common.core.constants.InterceptorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 访问判断是否公开拦截器
 *
 * @author TONE
 * @date 2024/8/29
 */
@Slf4j
@Order(2)
@Component
public class PathCheckFilter extends OncePerRequestFilter {

    @Resource
    private SysAuthProperties sysAuthProperties;

    /**
     * 对Uri判断是否开放的 注解
     *
     * @param request     request
     * @param response    response
     * @param filterChain filterChain
     * @return true-继续访问 false-返回结果
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("PathCheckInterceptor status={}", CurrentParam.get(CurrentParam.AUTH_STATUS_KEY));
        String url = request.getRequestURI();
        Integer authStatus = (Integer) CurrentParam.get(CurrentParam.AUTH_STATUS_KEY);
        if (CurrentParam.has(CurrentParam.OPEN_API_KEY) || sysAuthProperties.getOpenApis().stream().anyMatch(url::contains)) {
            CurrentParam.put(CurrentParam.AUTH_STATUS_KEY, authStatus | InterceptorCode.OPEN);
        }
        log.debug("PathCheckInterceptor status={}", CurrentParam.get(CurrentParam.AUTH_STATUS_KEY));
        filterChain.doFilter(request, response);
    }
}
