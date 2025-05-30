package com.haohaodayouxi.auth2.filter;

import com.haohaodayouxi.auth2.config.SysAuthProperties;
import com.haohaodayouxi.common.core.constants.CurrentParam;
import com.haohaodayouxi.common.core.constants.InterceptorCode;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

/**
 * Api访问权限拦截器
 *
 * @author TONE
 * @date 2024/8/29
 **/
@Slf4j
@Order(5)
@Component
public class ApiCheckFilter extends OncePerRequestFilter {

    @Resource
    private SysAuthProperties sysAuthProperties;


    /**
     * 用户访问的接口判断、检查用户的角色和菜单的关系
     *
     * @param request     request
     * @param response    response
     * @param filterChain filterChain
     * @return true-继续访问 false-返回结果
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("ApiCheckInterceptor status={}", CurrentParam.get(CurrentParam.AUTH_STATUS_KEY));
        // 10 authStatus=2   公开接口，token无效，无用户信息
        // 110 authStatus=6  公开接口，token有效，用户信息不可用
        // 1100 authStatus=12  非公开接口，token有效，用户信息可用
        // 1110 authStatus=14  公开接口，token有效，用户信息可用
        Integer authStatus = (Integer) CurrentParam.get(CurrentParam.AUTH_STATUS_KEY);
        // 2&12=0  6&12=4  12&12=12  14&12=12
        if (Objects.equals(authStatus, InterceptorCode.USER_TOKEN)) {
            // 检查用户的角色和菜单的关系
            String url = request.getRequestURI();
            if (!CurrentParam.has(CurrentParam.WHITE_API_KEY) && sysAuthProperties.getWhiteApis().stream().anyMatch(url::contains)) {
                // 不在白名单内，继续根据权限缓存判断是否可访问
//                if(接口不可访问) {
//                    log.error("接口不可访问");
//                    InterceptorErrorResponse.responseErrorByCode(response, InterceptorCode.API);
//                    return;
//                }
            }
        }
        CurrentParam.put(CurrentParam.AUTH_STATUS_KEY, authStatus | InterceptorCode.API);
        // 10010 authStatus=2   公开接口，token无效，无用户信息，接口可以访问
        // 10110 authStatus=6  公开接口，token有效，用户信息不可用，接口可以访问
        // 11100 authStatus=12  非公开接口，token有效，用户信息可用，接口可以访问
        // 11110 authStatus=14  公开接口，token有效，用户信息可用，接口可以访问
        log.debug("ApiCheckInterceptor status={}", CurrentParam.get(CurrentParam.AUTH_STATUS_KEY));
        filterChain.doFilter(request, response);
    }
}
