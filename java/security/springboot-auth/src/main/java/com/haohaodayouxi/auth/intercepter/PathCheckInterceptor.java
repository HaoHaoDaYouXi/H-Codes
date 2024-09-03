package com.haohaodayouxi.auth.intercepter;

import com.haohaodayouxi.auth.config.SysAuthProperties;
import com.haohaodayouxi.common.core.constants.CurrentParam;
import com.haohaodayouxi.common.core.constants.InterceptorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 访问判断是否公开拦截器
 *
 * @author TONE
 * @date 2024/8/29
 */
@Slf4j
@Component
public class PathCheckInterceptor implements HandlerInterceptor {

    @Resource
    private SysAuthProperties sysAuthProperties;

    /**
     * 对Uri判断是否开放的 注解
     *
     * @param request  request
     * @param response response
     * @param handler  handler
     * @return true-继续访问 false-返回结果
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.debug("PathCheckInterceptor status={}", CurrentParam.get(CurrentParam.AUTH_STATUS_KEY));
        String url = request.getRequestURI();
        Integer authStatus = (Integer) CurrentParam.get(CurrentParam.AUTH_STATUS_KEY);
        if (CurrentParam.has(CurrentParam.OPEN_API_KEY) || sysAuthProperties.getOpenApis().stream().anyMatch(url::contains)) {
            CurrentParam.put(CurrentParam.AUTH_STATUS_KEY, authStatus | InterceptorCode.OPEN);
        }
        log.debug("PathCheckInterceptor status={}", CurrentParam.get(CurrentParam.AUTH_STATUS_KEY));
        return true;
    }

}
