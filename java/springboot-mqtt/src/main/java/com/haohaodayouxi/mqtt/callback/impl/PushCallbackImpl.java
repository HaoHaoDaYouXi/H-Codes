package com.haohaodayouxi.mqtt.callback.impl;

import com.haohaodayouxi.mqtt.callback.TemplateCallback;
import com.haohaodayouxi.mqtt.service.MqttService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

/**
 * 自定义回调函数
 *
 * @author lenovo
 */
@Slf4j
@Component
public class PushCallbackImpl extends TemplateCallback {
    @Resource
    private MqttService mqttService;

    @Override
    public void connectComplete(boolean b, String s) {
        super.connectComplete(b, s);
    }

    @Override
    public void connectionLost(Throwable cause) {
        super.connectionLost(cause);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        super.deliveryComplete(token);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        super.messageArrived(topic, message);
        mqttService.consumeMsg(topic, new String(message.getPayload()));
    }

}
