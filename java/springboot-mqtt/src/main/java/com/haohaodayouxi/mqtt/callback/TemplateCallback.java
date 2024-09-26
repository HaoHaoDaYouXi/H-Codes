package com.haohaodayouxi.mqtt.callback;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * 发布消息的回调类
 * <p>
 * 必须实现MqttCallback的接口并实现对应的相关接口方法CallBack 类将实现 MqttCallBack。
 * 每个客户机标识都需要一个回调实例。在此示例中，构造函数传递客户机标识以另存为实例数据。
 * 在回调中，将它用来标识已经启动了该回调的哪个实例。
 * 必须在回调类中实现三个方法：
 * <p>
 * public void messageArrived(MqttTopic topic, MqttMessage message)接收已经预订的发布。
 * <p>
 * public void connectionLost(Throwable cause)在断开连接时调用。
 * <p>
 * public void deliveryComplete(MqttDeliveryToken token))
 * 接收到已经发布的 QoS 1 或 QoS 2 消息的传递令牌时调用。
 * 由 MqttClient.connect 激活此回调。
 *
 * @author TONE
 * @date 2024/9/25
 */
@Slf4j
public class TemplateCallback implements MqttCallbackExtended {
    /**
     * 连接完成后的回调
     * 只有客户端是清除session的才需要实现补充订阅
     *
     * @param b 连接状态
     * @param s mqtt地址
     */
    @Override
    public void connectComplete(boolean b, String s) {
        log.debug("success:{};url:{}", b, s);
    }

    /**
     * 连接断开时的回调
     * 连接丢失后，一般在这里面进行重连
     * 设置了自动重连可以不用再次实现重连
     *
     * @param cause 原因
     */
    @Override
    public void connectionLost(Throwable cause) {
        log.error("断开原因:{}", cause.toString());
    }

    /**
     * 收到下推消息时的回调
     * subscribe 后得到的消息会执行到这里面
     *
     * @param topic   主题
     * @param message 消息对象
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) {
        log.debug("接收消息主题:{};", topic);
        log.debug("接收消息Id:{};", message.getId());
        log.debug("接收消息Qos:{};", message.getQos());
        log.debug("接收消息内容:{};", new String(message.getPayload()));
    }

    /**
     * 消息发送成功时的回调
     * 当其中一个接收到就会触发
     *
     * @param token Mqtt 交付令牌
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        log.debug("isComplete:{};", token.isComplete());
    }
}
