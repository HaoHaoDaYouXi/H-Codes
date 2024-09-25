package com.haohaodayouxi.mqtt.config;

import com.haohaodayouxi.mqtt.callback.impl.PushCallbackImpl;
import com.haohaodayouxi.mqtt.constants.MqttConnectOptionProperties;
import com.haohaodayouxi.mqtt.constants.MqttConstant;
import com.haohaodayouxi.mqtt.utils.MqttUtil;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ClientMQTTConfig
 *
 * @author lenovo
 * @date 2022/5/27
 */
@Slf4j
@Configuration
public class MqttConfig {
    @Resource
    private MqttConnectOptionProperties mqttConnectOptionProperties;
    @Resource
    private PushCallbackImpl pushCallback;

    /**
     * 生成 默认配置的MqttClient
     *
     * @return MqttClient
     */
    @Bean
    public MqttClient initDefaultMqttClient() {
        if (MqttConstant.MQTT_CLIENT_MAP.containsKey(mqttConnectOptionProperties.getClientId())) {
            return MqttConstant.MQTT_CLIENT_MAP.get(mqttConnectOptionProperties.getClientId());
        }
        MqttClient client = MqttUtil.baseMqttClient(mqttConnectOptionProperties.getClientId(),
                mqttConnectOptionProperties.getUsername(),
                mqttConnectOptionProperties.getPassword(),
                mqttConnectOptionProperties.getUrl(),
                mqttConnectOptionProperties.getConnectionTimeOut(),
                mqttConnectOptionProperties.getKeepAliveInterval(),
                mqttConnectOptionProperties.getAutoReconnect(),
                mqttConnectOptionProperties.getCleanSession()
        );
        client.setCallback(pushCallback);
        MqttConstant.ALL_TOPIC.addAll(Arrays.asList(mqttConnectOptionProperties.getDefaultSubTopic().split(",")));
        try {
            List<String> topics = new ArrayList<>(); // 根据 clientId 查询数据库里客户端对应的主题
            if (ObjectUtils.isNotEmpty(topics)) {
                MqttConstant.ALL_TOPIC.addAll(topics.stream().filter(ObjectUtils::isNotEmpty).map(MqttConstant::firmSubTopicConvert).distinct().collect(Collectors.toList()));
            }
        } catch (Exception e) {
            log.error("获取厂商主题异常：{}", e.getMessage());
        }
        MqttUtil.subscribe(client, MqttConstant.ALL_TOPIC.toArray(new String[MqttConstant.ALL_TOPIC.size()]));
        MqttConstant.MQTT_CLIENT_MAP.put(mqttConnectOptionProperties.getClientId(), client);
        return client;
    }

    @PreDestroy
    public void destroyMqtt() {
        MqttUtil.close(MqttConstant.MQTT_CLIENT_MAP.get(mqttConnectOptionProperties.getClientId()));
    }


}
