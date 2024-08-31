package com.haohaodayouxi.shiro.config.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * 自定义密码校验，默认会调用shiro默认的
 *
 * @author TONE
 * @date 2024/8/26
 */
public class ShiroCredentialsMatcher extends SimpleCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken tokenResolve = (UsernamePasswordToken) token;
        String tokenPwd = new String(tokenResolve.getPassword());
        String infoPwd = (String) info.getCredentials();
        // 调用当前类重写的equals方法来对比两个password是否一致，返回对比结果
        return super.equals(tokenPwd, infoPwd);
    }
}
