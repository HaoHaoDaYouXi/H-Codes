package com.haohaodayouxi.auth2.filter;

import com.alibaba.fastjson2.JSON;
import com.haohaodayouxi.common.core.constants.InterceptorCode;
import com.haohaodayouxi.common.core.enums.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Objects;

/**
 * 根据拦截器返回的code返回提示信息
 *
 * @author TONE
 * @date 2024/8/29
 **/
@Slf4j
public class InterceptorErrorResponse {
    /**
     * 返回提示
     *
     * @param response   res
     * @param authStatus 认证状态
     */
    public static void responseErrorByCode(HttpServletResponse response, Integer authStatus) {
        try {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            // 第三位标记token数据问题 第四位标记token获取user数据有问题
            if (Objects.equals(authStatus, InterceptorCode.UN_OPEN)) {
                response.getWriter().print(JSON.toJSONString(ErrorResponse.BAN_ERROR.toResponse()));
            } else if ((authStatus & InterceptorCode.TOKEN) > 0 || (authStatus & InterceptorCode.USER) > 0) {
                response.getWriter().print(JSON.toJSONString(ErrorResponse.ILLEGAL_TOKEN.toResponse("请先登录")));
            } else if ((authStatus & InterceptorCode.API) > 0) {
                response.getWriter().print(JSON.toJSONString(ErrorResponse.PERMISSION_DENIED.toResponse()));
            } else if ((authStatus & InterceptorCode.DATA) > 0) {
                response.getWriter().print(JSON.toJSONString(ErrorResponse.PERMISSION_DENIED.toResponse()));
            }
            response.flushBuffer();
        } catch (IOException e) {
            log.error("response error code 异常！", e);
        }
    }

    /**
     * 直接返回异常信息
     *
     * @param response
     * @param errMsg
     */
    public static void responseErrorMsg(HttpServletResponse response, String errMsg) {
        try {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().print(JSON.toJSONString(ErrorResponse.ILLEGAL_TOKEN.toResponse(errMsg)));
            response.flushBuffer();
        } catch (IOException e) {
            log.error("response error code 异常！", e);
        }
    }
}
