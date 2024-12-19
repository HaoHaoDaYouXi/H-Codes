package com.haohaodayouxi.auth.intercepter;

import com.haohaodayouxi.common.core.annotation.OpenApi;
import com.haohaodayouxi.common.core.annotation.PermissionApi;
import com.haohaodayouxi.common.core.annotation.TokenApi;
import com.haohaodayouxi.common.core.annotation.WhiteApi;
import com.haohaodayouxi.common.core.constants.CurrentParam;
import com.haohaodayouxi.common.core.constants.InterceptorCode;
import com.haohaodayouxi.common.util.business.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 基础拦截器
 * 用于将参数装载到ThreadLocal的CurrentParam中
 *
 * @author TONE
 * @date 2024/8/29
 */
@Slf4j
@Component
public class InitParamInterceptor implements HandlerInterceptor {

    /**
     * 基础拦截器 判断请求和参数装载进缓存
     *
     * @param request  request
     * @param response response
     * @param handler  handler
     * @return true-继续访问 false-返回结果
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.debug("InitParamInterceptor");
        CurrentParam.reset();

        // 获取注解类，并将其装入CurrentParam
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 获取类注解
        Class<?> clazz = handlerMethod.getBeanType();
        Annotation[] classAnnotations = clazz.getAnnotations();
        parseAnnotations(classAnnotations);
        // 获取方法注解
        Method method = handlerMethod.getMethod();
        Annotation[] methodAnnotations = method.getAnnotations();
        parseAnnotations(methodAnnotations);

        // 获取token，并将其装入currentParam
        String authToken = TokenUtil.getToken(request);
        CurrentParam.put(CurrentParam.AUTH_TOKEN_KEY, authToken);
        // 标记状态为正常
        CurrentParam.put(CurrentParam.AUTH_STATUS_KEY, InterceptorCode.UN_OPEN);
        log.debug("InitParamInterceptor status={}", CurrentParam.get(CurrentParam.AUTH_STATUS_KEY));
        return true;
    }

    /**
     * 解析注解列表，将注解保存到线程变量中
     */
    private void parseAnnotations(Annotation[] methodAnnotations) {
        for (Annotation item : methodAnnotations) {
            if (item instanceof OpenApi api) {
                CurrentParam.put(CurrentParam.OPEN_API_KEY, api);
            }
            if (item instanceof TokenApi api) {
                CurrentParam.put(CurrentParam.TOKEN_API_KEY, api);
            }
            if (item instanceof WhiteApi api) {
                CurrentParam.put(CurrentParam.WHITE_API_KEY, api);
            }
            if (item instanceof PermissionApi api) {
                CurrentParam.put(CurrentParam.PERMISSION_API_KEY, api);
            }
        }
    }
}
