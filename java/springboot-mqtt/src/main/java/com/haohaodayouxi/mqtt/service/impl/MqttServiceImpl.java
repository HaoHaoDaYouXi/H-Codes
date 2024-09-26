package com.haohaodayouxi.mqtt.service.impl;

import com.haohaodayouxi.mqtt.callback.TemplateCallback;
import com.haohaodayouxi.mqtt.constants.MqttConstant;
import com.haohaodayouxi.mqtt.service.MqttService;
import com.haohaodayouxi.mqtt.utils.MqttUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * MqttServiceImpl
 *
 * @author TONE
 * @date 2024/9/25
 */
@Slf4j
@Service
public class MqttServiceImpl implements MqttService {
    @Resource
    private MqttClient client;

    @Override
    public void pushMsg(String topic, String code, String msg) {
        pushMsg(topic, code, msg, MqttConstant.QOS_1, false);
    }

    @Override
    public void pushMsg(String topic, String code, String msg, int qos) {
        pushMsg(topic, code, msg, qos, false);
    }

    @Override
    public void pushMsg(String topic, String code, String msg, boolean retained) {
        pushMsg(topic, code, msg, MqttConstant.QOS_1, retained);
    }

    @Override
    public void pushMsg(String topic, String code, String msg, int qos, boolean retained) {
        boolean boo = MqttUtil.basePublish(client, MqttConstant.operateTopicConvert(topic, code), msg, qos, retained);
        // 当下发失败时，再次补发一次
        if (!boo) {
            MqttUtil.basePublish(client, MqttConstant.operateTopicConvert(topic, code), msg, qos, retained);
        }
    }

    @Override
    public void pushMsgByClient(String url, String user, String pwd, String topic, String msg) {
        pushMsgByClient(url, user, pwd, topic, msg, MqttConstant.QOS_1, false);
    }

    @Override
    public void pushMsgByClient(String url, String user, String pwd, String topic, String msg, int qos) {
        pushMsgByClient(url, user, pwd, topic, msg, qos, false);
    }

    @Override
    public void pushMsgByClient(String url, String user, String pwd, String topic, String msg, boolean retained) {
        pushMsgByClient(url, user, pwd, topic, msg, MqttConstant.QOS_1, retained);
    }

    @Override
    public void pushMsgByClient(String url, String user, String pwd, String topic, String msg, int qos, boolean retained) {
        MqttClient newClient = MqttUtil.baseMqttClient(new TemplateCallback(), UUID.randomUUID().toString(), user, pwd, url, 10, 20, true, false);
        boolean boo = MqttUtil.basePublish(newClient, topic, msg, qos, retained);
        // 当下发失败时，再次补发一次
        if (!boo) {
            MqttUtil.basePublish(newClient, topic, msg, qos, retained);
        }
        MqttUtil.close(newClient);
    }

    @Override
    public void appendSubTopic(String... topic) {
        List<String> data = Arrays.stream(topic).map(MqttConstant::firmSubTopicConvert).collect(Collectors.toList());
        MqttUtil.baseUnSubscribe(client, data.toArray(new String[data.size()]));
        MqttUtil.subscribe(client, data.toArray(new String[data.size()]));
        MqttConstant.ALL_TOPIC.addAll(data);
    }

    @Override
    public void subTopic(String... topic) {
        MqttUtil.baseUnSubscribe(client, MqttConstant.ALL_TOPIC.toArray(new String[MqttConstant.ALL_TOPIC.size()]));
        MqttConstant.ALL_TOPIC = Arrays.stream(topic).map(MqttConstant::firmSubTopicConvert).collect(Collectors.toSet());
        MqttUtil.subscribe(client, MqttConstant.ALL_TOPIC.toArray(new String[MqttConstant.ALL_TOPIC.size()]));
    }

    @Override
    public void unSubTopic(String... topic) {
        List<String> data = Arrays.stream(topic).map(MqttConstant::firmSubTopicConvert).collect(Collectors.toList());
        MqttConstant.ALL_TOPIC.removeAll(data);
        MqttUtil.baseUnSubscribe(client, data.toArray(new String[data.size()]));
    }

    @Override
    public void consumeMsg(String topic, String msg) {
        try {
            // 消费消息
            if (topic.startsWith(MqttConstant.TEST_TOPIC)) {
                // 测试主题
                log.info("测试主题,topic:{},msg:{}", topic, msg);
            } else if (topic.endsWith(MqttConstant.STATUS_TOPIC)) {
                statusTopic(topic, msg);
            } else if (topic.endsWith(MqttConstant.OPERATE_TOPIC)) {
                // 调用更改设备运行状态的接口
                log.debug("操作下发主题,topic:{},msg:{}", topic, msg);
            } else if (topic.endsWith(MqttConstant.OPERATE_TOPIC_BACK)) {
                // 调用更改设备运行状态的接口
                operateTopic(topic, msg);
            } else if (topic.endsWith(MqttConstant.DATA_TOPIC)) {
                // 数据主题
                dataTopic(topic, msg);
            } else {
                // 其余主题默认错误的主题 不处理
                log.error("错误主题,topic:{},msg:{}", topic, msg);
            }
        } catch (Exception e) {
            log.error("消息处理异常!", e);
        }
    }

    /**
     * 设备状态
     *
     * @param topic
     * @param msg
     */
    private void statusTopic(String topic, String msg) {
        // 状态主题
        log.debug("状态主题,topic:{},msg:{}", topic, msg);
        try {
            // 截取 设备编号
            String deviceCode = topic.substring(topic.indexOf("/") > 0 ? (topic.indexOf("/") + 1) : 0,
                    topic.indexOf("/", (topic.indexOf("/") + 1)) > 0 ? topic.indexOf("/", (topic.indexOf("/") + 1)) : topic.length());
            // 状态码为在线时更新运行状态，其他都为掉线
            if (MqttConstant.MQTT_CLIENT_ONLINE.equals(msg)) {
                log.debug("设备上线,topic:{},msg:{}", topic, msg);
            } else {
                log.error("设备掉线,topic:{},msg:{}", topic, msg);
            }
        } catch (Exception e) {
            log.error("状态主题异常", e);
        }
    }

    /**
     * 设备操作 更新状态
     *
     * @param topic
     * @param msg
     */
    private void operateTopic(String topic, String msg) {
        try {
            // 操作主题 设备操作状态变动 需要变动设备的状态
            log.debug("操作主题,topic:{},msg:{}", topic, msg);
            // 截取 设备编号
            String deviceCode = topic.substring(topic.indexOf("/") > 0 ? (topic.indexOf("/") + 1) : 0,
                    topic.indexOf("/", (topic.indexOf("/") + 1)) > 0 ? topic.indexOf("/", (topic.indexOf("/") + 1)) : topic.length());
            // 状态码为启动时更新状态，其他都为停止
            if (MqttConstant.MQTT_DEVICE_START.equals(msg)) {
                // 更新设备状态
                log.debug("设备启动,topic:{},msg:{}", topic, msg);
            } else {
                log.error("设备停止,topic:{},msg:{}", topic, msg);
            }
        } catch (Exception e) {
            log.error("操作主题异常", e);
        }
    }

    /**
     * 数据主题
     *
     * @param topic
     * @param msg
     */
    private void dataTopic(String topic, String msg) {
        try {
            // 插入设备数据
            log.debug("数据主题,topic:{},msg:{}", topic, msg);
        } catch (Exception e) {
            log.error("数据主题异常", e);
        }
    }

}
