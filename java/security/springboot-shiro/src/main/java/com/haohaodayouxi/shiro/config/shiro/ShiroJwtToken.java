package com.haohaodayouxi.shiro.config.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * ShiroJwtToken
 *
 * @author TONE
 * @date 2024/8/27
 **/
public class ShiroJwtToken implements AuthenticationToken {

    private static final long serialVersionUID = 1L;
    private String token;

    public ShiroJwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
