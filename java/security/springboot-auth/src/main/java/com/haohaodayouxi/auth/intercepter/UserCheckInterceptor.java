package com.haohaodayouxi.auth.intercepter;

import com.haohaodayouxi.auth.model.bo.LoginCacheBO;
import com.haohaodayouxi.common.core.constants.CurrentParam;
import com.haohaodayouxi.common.core.constants.CurrentUserContextHolder;
import com.haohaodayouxi.common.core.constants.InterceptorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 用户缓存信息校验
 *
 * @author TONE
 * @date 2024/8/29
 **/
@Slf4j
@Component
public class UserCheckInterceptor implements HandlerInterceptor {


    /**
     * 对用户缓存信息校验，判断用户账号是否可访问
     * 以及缓存是否需要变动和更新缓存有效期
     *
     * @param request  request
     * @param response response
     * @param handler  handler
     * @return true-继续访问 false-返回结果
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.debug("UserCheckInterceptor status={}", CurrentParam.get(CurrentParam.AUTH_STATUS_KEY));
        // 10 authStatus=2   公开接口，token无效
        // 100 authStatus=4  非公开接口，token有效
        // 110 authStatus=6  公开接口，token有效
        Integer authStatus = (Integer) CurrentParam.get(CurrentParam.AUTH_STATUS_KEY);
        // 根据认证状态，获取上一步已取出的用户信息，判断用户是否可访问
        // 2&4=0   4&4=4   6&4=4
        if ((authStatus & InterceptorCode.TOKEN) != 0) {
            // todo 用户不可访问时直接返回错误信息 此处根据不能访问的用户、角色、身份等等缓存进行判断，
            //  尽量不要涉及到数据库查询，统一通过缓存进行判断，缓存内提前记录不能访问的一些限制标志，用作此处判断
            LoginCacheBO bo = (LoginCacheBO) CurrentUserContextHolder.get();
            // 查询用户信息是否还可以使用
            // ...todo查询用户是否有不可使用标识
            if (ObjectUtils.isEmpty(bo)) {
                // 用户信息不可以使用情况，并且接口为非公开接口
                if (authStatus.equals(InterceptorCode.TOKEN)) {
                    log.error("用户不可以使用，无法访问");
                    InterceptorErrorResponse.responseErrorByCode(response, InterceptorCode.USER);
                    return false;
                }
                // 6
            } else {
                // 4|8=12   6|8=14
                CurrentParam.put(CurrentParam.AUTH_STATUS_KEY, authStatus | InterceptorCode.USER);
            }
        }
        // 10 authStatus=2   公开接口，token无效，无用户信息
        // 110 authStatus=6  公开接口，token有效，用户信息不可用
        // 1100 authStatus=12  非公开接口，token有效，用户信息可用
        // 1110 authStatus=14  公开接口，token有效，用户信息可用
        log.debug("UserCheckInterceptor status={}", CurrentParam.get(CurrentParam.AUTH_STATUS_KEY));
        return true;
    }
}
