package com.haohaodayouxi.auth2.filter;

import com.haohaodayouxi.auth2.config.SysAuthProperties;
import com.haohaodayouxi.auth2.model.bo.LoginCacheBO;
import com.haohaodayouxi.common.core.constants.CurrentParam;
import com.haohaodayouxi.common.core.constants.CurrentUserContextHolder;
import com.haohaodayouxi.common.core.constants.InterceptorCode;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Token有效校验拦截器，此处只判断token是否有效，不做具体的用户数据校验
 *
 * @author TONE
 * @date 2024/8/29
 */
@Slf4j
@Order(3)
@Component
public class TokenCheckFilter extends OncePerRequestFilter {

    @Resource
    private SysAuthProperties sysAuthProperties;

    /**
     * 对token判断是否有效，并放入缓存
     * 以及缓存是否需要变动和更新缓存有效期
     *
     * @param request     request
     * @param response    response
     * @param filterChain filterChain
     * @return true-继续访问 false-返回结果
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("TokenCheckInterceptor status={}", CurrentParam.get(CurrentParam.AUTH_STATUS_KEY));
        // 清空线程内的token用户缓存信息，并添加新的
        CurrentUserContextHolder.set(null);
        // 0-00 非公开接口 2-10 公开接口
        Integer authStatus = (Integer) CurrentParam.get(CurrentParam.AUTH_STATUS_KEY);
        // 检查token是否有效，非公开接口必须要token有效，公开接口可以不要token
        boolean tokenInvalid = true;
        String token = (String) CurrentParam.get(CurrentParam.AUTH_TOKEN_KEY);
        String url = request.getRequestURI();
        log.debug("接口：{}", url);
        // 若有@TokenApi注解，或者是配置的token接口，则不需要获取 token，避免token接口循环获取token
        if (!CurrentParam.has(CurrentParam.TOKEN_API_KEY) && sysAuthProperties.getTokenUris().stream().noneMatch(url::equals)) {
            log.debug("需要获取token");
            if (ObjectUtils.isNotEmpty(token)) {
                // 调用接口判断token是否还在缓存内，并把缓存对象放入线程对象内
                // 只通过缓存判断token是否还可以使用，用户的信息和权限在后续的校验中处理
                // 如果要做token被删除，需要提示用户信息的，可以在此处做提示
                LoginCacheBO cacheBO = new LoginCacheBO();
                if (ObjectUtils.isNotEmpty(cacheBO)) {
                    CurrentUserContextHolder.set(cacheBO);
                    tokenInvalid = false;
                    CurrentParam.put(CurrentParam.AUTH_STATUS_KEY, authStatus | InterceptorCode.TOKEN);
                }
            }
        } else {
            log.debug("不需要获取token，避免token接口循环获取token");
        }
        // token无效 && 非公开接口
        // 0&2=0 非公开  2&2=2 公开
        if (tokenInvalid && (authStatus & InterceptorCode.OPEN) == 0) {
            log.error("token={} 无效 && 非公开接口", token);
            InterceptorErrorResponse.responseErrorByCode(response, InterceptorCode.TOKEN);
            return;
        }
        // 10 authStatus=2   公开接口，token无效
        // 100 authStatus=4  非公开接口，token有效
        // 110 authStatus=6  公开接口，token有效
        log.debug("TokenCheckInterceptor status={}", CurrentParam.get(CurrentParam.AUTH_STATUS_KEY));
        filterChain.doFilter(request, response);
    }
}
