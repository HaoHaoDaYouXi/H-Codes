package com.haohaodayouxi.auth.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 系统认证配置
 *
 * @author TONE
 * @date 2024/8/29
 */
@Data
@Configuration
@ConfigurationProperties("h.sys.auth")
public class SysAuthProperties implements Serializable {
    @Serial
    private static final long serialVersionUID = -7218460583434903529L;
    /**
     * 开放的接口地址，包含就认为是开放的，公开接口，可以没有token访问
     */
    @Value("${h.sys.auth.openApis:/error,/h-open/}")
    private List<String> openApis;

    /**
     * token接口地址，跳过获取token，避免死循环获取token
     */
    @Value("${h.sys.auth.tokenUris:/user/login/getByToken}")
    private List<String> tokenUris;

    /**
     * 白名单Api，不需要校验接口权限，例如：获取token接口，获取菜单接口等
     */
    @Value("${h.sys.auth.whiteApis:/user/login/getByToken,/res/getRouterByToken}")
    private List<String> whiteApis;

}
