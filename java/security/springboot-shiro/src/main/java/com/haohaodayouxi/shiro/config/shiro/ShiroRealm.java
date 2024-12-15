package com.haohaodayouxi.shiro.config.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * ShiroRealm
 *
 * @author TONE
 * @date 2024/8/26
 */
@Slf4j
public class ShiroRealm extends AuthorizingRealm {

    /**
     * Shiro权限认证,授权(权限操作验证,只有请求设置特定权限时调用此方法)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        ShiroUserInfo user = (ShiroUserInfo) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        // 若要实时更新权限，每次都重新查询
        if (ObjectUtils.isNotEmpty(user.getRoles())) {
            simpleAuthorizationInfo.addRoles(user.getRoles());
        }
        if (ObjectUtils.isNotEmpty(user.getPermissions())) {
            simpleAuthorizationInfo.addStringPermissions(user.getPermissions());
        }
        return simpleAuthorizationInfo;
    }

    /**
     * Shiro登录认证(原理：用户提交 用户名和密码  --- shiro 封装token ---- realm 通过用户名将密码查询返回 ---- shiro 自动去比较查询出密码和用户输入密码是否一致---- 进行登陆控制 )
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken uToken = (UsernamePasswordToken) authenticationToken;// 获取用户输入的token
        String username = uToken.getUsername();
        // 根据username，查询数据库用户信息
//        if (账号不存在判断) {
//            throw new UnknownAccountException();
//        }
//        if (账号禁用判断) {
//            throw new DisabledAccountException();
//        }
        // 封装用户信息
        ShiroUserInfo userInfo = new ShiroUserInfo();
        // 查询用户权限，并赋值
        // userInfo.setRoles();         @RequiresRoles("admin")  若通过过滤器进行权限校验，可以接口上不写注解
        // userInfo.setPermissions();   @RequiresPermissions("/userInfo")  若通过过滤器进行权限校验，可以接口上不写注解
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(userInfo, userInfo.getPwd(), getName());
        // 设置盐值
        // info.setCredentialsSalt(ByteSource.Util.bytes(userInfo.getCode()));
        // 自定义密码校验 需要继承SimpleCredentialsMatcher 重写 doCredentialsMatch 方法
        return info;
    }

}
