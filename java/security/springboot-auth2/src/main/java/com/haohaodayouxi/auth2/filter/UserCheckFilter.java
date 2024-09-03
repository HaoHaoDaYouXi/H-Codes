package com.haohaodayouxi.auth2.filter;

import com.haohaodayouxi.common.core.constants.CurrentParam;
import com.haohaodayouxi.common.core.constants.CurrentUserContextHolder;
import com.haohaodayouxi.common.core.constants.InterceptorCode;
import com.haohaodayouxi.common.core.model.bo.LoginCacheBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户缓存信息校验
 *
 * @author TONE
 * @date 2024/8/29
 **/
@Slf4j
@Order(4)
@Component
public class UserCheckFilter extends OncePerRequestFilter {

    /**
     * 对用户缓存信息校验，判断用户账号是否可访问
     * 以及缓存是否需要变动和更新缓存有效期
     *
     * @param request     request
     * @param response    response
     * @param filterChain filterChain
     * @return true-继续访问 false-返回结果
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("UserCheckInterceptor status={}", CurrentParam.get(CurrentParam.AUTH_STATUS_KEY));
        // 10 authStatus=2   公开接口，token无效
        // 100 authStatus=4  非公开接口，token有效
        // 110 authStatus=6  公开接口，token有效
        Integer authStatus = (Integer) CurrentParam.get(CurrentParam.AUTH_STATUS_KEY);
        // 根据认证状态，获取上一步已取出的用户信息，判断用户是否可访问
        // 2&4=0   4&4=4   6&4=4
        if ((authStatus & InterceptorCode.TOKEN) != 0) {
            LoginCacheBO bo = CurrentUserContextHolder.get();
            // todo 用户不可访问时直接返回错误信息 此处根据不能访问的用户、角色、身份等等缓存进行判断，
            //  尽量不要涉及到数据库查询，统一通过缓存进行判断，缓存内提前记录不能访问的一些限制标志，用作此处判断
//            if(用户不可访问) {
//                log.error("用户不可访问");
//                InterceptorErrorResponse.responseErrorByCode(response, InterceptorCode.USER);
//                return;
//            }
        }
        CurrentParam.put(CurrentParam.AUTH_STATUS_KEY, authStatus | InterceptorCode.USER);
        // 1010 authStatus=10  公开接口，token无效，用户可访问
        // 1100 authStatus=12  非公开接口，token有效，用户可访问
        // 1110 authStatus=14  公开接口，token有效，用户可访问
        log.debug("UserCheckInterceptor status={}", CurrentParam.get(CurrentParam.AUTH_STATUS_KEY));
        filterChain.doFilter(request, response);
    }
}
