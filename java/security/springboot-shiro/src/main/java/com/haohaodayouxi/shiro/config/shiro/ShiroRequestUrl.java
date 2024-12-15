package com.haohaodayouxi.shiro.config.shiro;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import java.util.Set;

/**
 * ShiroRequestUrl
 * 也可通过 PermissionsAuthorizationFilter 实现
 *
 * @author TONE
 * @date 2024/8/27
 */
public class ShiroRequestUrl extends AccessControlFilter {

    /**
     * INDEX地址
     */
    private static final String INDEX = "/index.htm";
    /**
     * 403
     */
    private static final String Forbidden = "/error/403.htm";

    /**
     * 判断是否允许访问
     *
     * @param request  请求
     * @param response 响应
     * @param o        配置对象
     * @return true 允许访问，false 拒绝访问
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object o) {
        Subject user = SecurityUtils.getSubject();
        String url = getPathWithinApplication(request);
        // shiroUser可能为空，这是登录
        ShiroUserInfo shiroUser = (ShiroUserInfo) user.getPrincipal();
        // 权限校验 判断是否允许访问
        Set<String> urlSet = shiroUser.getPermissions();
        for (String s : urlSet) {
            if (url.endsWith(s)) {
                return true;
            }
        }
        return false; // 触发 onAccessDenied
    }

    /**
     * 访问拒绝
     *
     * @param servletRequest  请求
     * @param servletResponse 响应
     * @return true 继续往下走，false 拦截
     * @throws Exception 抛出异常
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        Subject user = SecurityUtils.getSubject();
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (!user.isAuthenticated()) {
            response.sendRedirect(request.getContextPath() + INDEX);
        } else {
            response.sendRedirect(request.getContextPath() + Forbidden);
        }
        return false;
    }

}
