package com.haohaodayouxi.shiro.controller;

import com.haohaodayouxi.common.core.annotation.OpenApi;
import com.haohaodayouxi.shiro.config.shiro.ShiroRealm;
import com.haohaodayouxi.shiro.config.shiro.ShiroUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * HealthController
 *
 * @author TONE
 * @date 2024/8/24
 */
@Slf4j
@RestController
@RequestMapping("/health")
public class HealthController {
    /**
     * 项目名称
     */
    @Value("${spring.application.name:demo}")
    private String applicationName;


    @GetMapping("/h-open/check")
    public String healthCheck() {
        log.info("healthCheck");
        return String.format(applicationName + " is health! %s", LocalDateTime.now());
    }

    /**
     * Shiro登录示例
     */
    @OpenApi
    @GetMapping("/shiroLogin")
    public void login() {
        Subject subject = SecurityUtils.getSubject();
        // if (subject.isAuthenticated())  判断是否已经登录认证了
        UsernamePasswordToken token = new UsernamePasswordToken("username", "password");
        subject.login(token); // 登录
        boolean hasRole = subject.hasRole("role"); // 判断是否拥有角色，具体内容是有登录验证时添加进去的
        boolean hasPermission = subject.isPermitted("user:insert"); // 判断是否拥有权限，具体内容是有登录验证时添加进去的
        //也可以用 subject.checkPermission("user:insert");   没有返回值，没权限抛 AuthenticationException

        ShiroUserInfo shiroUser = (ShiroUserInfo) subject.getPrincipal();  // 获取用户信息

        // 通过 principalCollection 切换用户
        SimplePrincipalCollection principals = new SimplePrincipalCollection(shiroUser, shiroUser.getLoginName());
        subject.runAs(principals); // 切换用户
        RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        ShiroRealm shiroRealm = (ShiroRealm) rsm.getRealms().iterator().next();
        // 删除认证缓存
        if (shiroRealm.isAuthenticationCachingEnabled()) {
            shiroRealm.getAuthenticationCache().remove(principals);
        }
        if (shiroRealm.isAuthorizationCachingEnabled()) {
            shiroRealm.getAuthorizationCache().remove(principals);
        }
        // 刷新权限
        subject.releaseRunAs();

    }

}
