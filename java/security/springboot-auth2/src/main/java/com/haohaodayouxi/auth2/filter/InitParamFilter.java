package com.haohaodayouxi.auth2.filter;

import com.haohaodayouxi.common.core.annotation.OpenApi;
import com.haohaodayouxi.common.core.annotation.TokenApi;
import com.haohaodayouxi.common.core.annotation.WhiteApi;
import com.haohaodayouxi.common.core.constants.CurrentParam;
import com.haohaodayouxi.common.core.constants.InterceptorCode;
import com.haohaodayouxi.common.util.business.TokenUtil;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
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
@Order(1)
@Component
public class InitParamFilter extends OncePerRequestFilter {

    private HandlerMapping handlerMapping;

    @PostConstruct
    public void init() {
        handlerMapping = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext()).getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
    }

    /**
     * 基础拦截器 判断请求和参数装载进缓存
     *
     * @param request     request
     * @param response    response
     * @param filterChain filterChain
     * @return true-继续访问 false-返回结果
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.debug("InitParamInterceptor");
        CurrentParam.reset();
        try {
            // 根据请求获取 handler链
            HandlerExecutionChain executionChain = handlerMapping.getHandler(request);
            if (executionChain != null) {
                // 获取注解类，并将其装入CurrentParam
                HandlerMethod handlerMethod = (HandlerMethod) executionChain.getHandler();
                // 获取类注解
                Class<?> clazz = handlerMethod.getBeanType();
                Annotation[] classAnnotations = clazz.getAnnotations();
                parseAnnotations(classAnnotations);
                // 获取方法注解
                Method method = handlerMethod.getMethod();
                Annotation[] methodAnnotations = method.getAnnotations();
                parseAnnotations(methodAnnotations);
            }
        } catch (Exception e) {
            log.error("根据请求无法找到对应的处理链", e);
            throw new RuntimeException("根据请求无法找到对应的处理链");
        }
        // 获取token，并将其装入currentParam
        String authToken = TokenUtil.getToken(request);
        CurrentParam.put(CurrentParam.AUTH_TOKEN_KEY, authToken);
        // 标记状态为正常
        CurrentParam.put(CurrentParam.AUTH_STATUS_KEY, InterceptorCode.UN_OPEN);
        log.debug("InitParamInterceptor status={}", CurrentParam.get(CurrentParam.AUTH_STATUS_KEY));
        // 继续过滤链
        filterChain.doFilter(request, response);
    }

    /**
     * 解析注解列表，将注解保存到线程变量中
     */
    private void parseAnnotations(Annotation[] methodAnnotations) {
        for (Annotation item : methodAnnotations) {
            if (item instanceof OpenApi) {
                OpenApi api = (OpenApi) item;
                CurrentParam.put(CurrentParam.OPEN_API_KEY, api);
            }
            if (item instanceof TokenApi) {
                TokenApi api = (TokenApi) item;
                CurrentParam.put(CurrentParam.TOKEN_API_KEY, api);
            }
            if (item instanceof WhiteApi) {
                WhiteApi api = (WhiteApi) item;
                CurrentParam.put(CurrentParam.WHITE_API_KEY, api);
            }
        }
    }
}
