package com.haohaodayouxi.auth.intercepter;

import com.haohaodayouxi.auth.config.SysAuthProperties;
import com.haohaodayouxi.common.core.constants.CurrentParam;
import com.haohaodayouxi.common.core.constants.CurrentUserContextHolder;
import com.haohaodayouxi.common.core.constants.InterceptorCode;
import com.haohaodayouxi.common.core.model.bo.LoginCacheBO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Token有效校验拦截器，此处只判断token是否有效，不做具体的用户数据校验
 *
 * @author TONE
 * @date 2024/8/29
 */
@Slf4j
@Component
public class TokenCheckInterceptor implements HandlerInterceptor {

    @Resource
    private SysAuthProperties sysAuthProperties;

    /**
     * 对token判断是否有效，并放入缓存
     * 以及缓存是否需要变动和更新缓存有效期
     *
     * @param request  request
     * @param response response
     * @param handler  handler
     * @return true-继续访问 false-返回结果
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 清空线程内的token用户缓存信息，并添加新的
        CurrentUserContextHolder.set(null);
        // 0-00 非公开接口 2-10 公开接口
        Integer authStatus = (Integer) CurrentParam.get(CurrentParam.AUTH_STATUS_KEY);
        // 检查token是否有效，非公开接口必须要token有效，公开接口可以不要token
        boolean tokenInvalid = true;
        String token = (String) CurrentParam.get(CurrentParam.AUTH_TOKEN_KEY);
        String url = request.getRequestURI();
        log.debug("token 校验接口{}", url);
        if (CurrentParam.has(CurrentParam.TOKEN_API_KEY) || sysAuthProperties.getTokenUris().stream().noneMatch(url::equals)) {
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
        }
        // token无效 && 非公开接口
        // 0&2=0 非公开  2&2=2 公开
        if (tokenInvalid && (authStatus & InterceptorCode.OPEN) == 0) {
            log.error("token={} 无效 && 非公开接口", token);
            InterceptorErrorResponse.responseErrorByCode(response, InterceptorCode.TOKEN);
            return false;
        }
        // 10 authStatus=2   公开接口，token无效
        // 100 authStatus=4  非公开接口，token有效
        // 110 authStatus=6  公开接口，token有效
        log.debug("token是否有效拦截器status={}", CurrentParam.get(CurrentParam.AUTH_STATUS_KEY));
        return true;
    }
}
