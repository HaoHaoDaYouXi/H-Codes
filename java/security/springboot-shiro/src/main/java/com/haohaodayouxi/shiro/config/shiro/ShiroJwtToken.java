package com.haohaodayouxi.shiro.config.shiro;

import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

import java.io.Serial;

/**
 * ShiroJwtToken
 *
 * @author TONE
 * @date 2024/8/27
 **/
@Data
public class ShiroJwtToken implements AuthenticationToken {
    @Serial
    private static final long serialVersionUID = -7919903568711935433L;

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
