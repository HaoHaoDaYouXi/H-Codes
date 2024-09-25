package com.haohaodayouxi.mqtt.constants;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * MqttProperties
 *
 * @author lenovo
 * @date 2022/5/24
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "h.mqtt.connect-option")
public class MqttConnectOptionProperties {
    /**
     * 地址
     */
    private String url = "tcp://127.0.0.1:1883";
    /**
     * 用户名
     */
    private String username = "";
    /**
     * 密码
     */
    private String password = "";
    /**
     * 超时时间 单位为秒
     */
    private Integer connectionTimeOut = 10;
    /**
     * 重新连接
     */
    private Integer keepAliveInterval = 20;
    /**
     * 重新连接
     */
    private Boolean autoReconnect = true;
    /**
     * 清空session
     */
    private Boolean cleanSession = false;
    /**
     * 客户端 ID 唯一标识
     */
    private String clientId;
    /**
     * 默认订阅主题 可以同时消费（订阅）多个Topic ,分割
     */
    private String defaultSubTopic;
    /**
     * 默认发布主题
     */
    private String defaultPubTopic;
}
