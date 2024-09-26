package com.haohaodayouxi.mqtt.constants;

import org.apache.commons.lang3.ObjectUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;

import java.util.*;

/**
 * MqttConstant
 *
 * @author TONE
 * @date 2024/9/25
 */
public class MqttConstant {
    /**
     * MQTT 通道缓存
     */
    public static Map<String, MqttClient> MQTT_CLIENT_MAP = new HashMap<>(32);
    /**
     * 累计订阅的主题
     * 重新订阅时使用
     */
    public static Set<String> ALL_TOPIC = new HashSet<>(32);
    /**
     * 累计订阅的主题
     * 重新订阅时使用
     */
    public static Set<Integer> ALL_QOS = new HashSet<>(8);
    /**
     * qos 0
     */
    public static final int QOS_0 = 0;
    /**
     * qos 1
     */
    public static final int QOS_1 = 1;
    /**
     * qos 2
     */
    public static final int QOS_2 = 2;
    /**
     * 遗嘱 话题
     */
    public static final String WILL_TOPIC = "h_mqtt_will_topic";
    /**
     * 设置“遗嘱”消息的话题，若客户端与服务器之间的连接意外中断，服务器将发布客户端的“遗嘱”消息。
     */
    public static final String WILL_DATA = "off_line";
    /**
     * 数据主题
     */
    public static final String DATA_TOPIC = "/data_topic";
    /**
     * 状态主题
     */
    public static final String STATUS_TOPIC = "status_topic";
    /**
     * 操作主题(下发给客户端使用)  中间主题
     */
    public static final String OPERATE_TOPIC = "operate_topic";
    /**
     * 操作主题(客户端反馈使用) 中间主题
     */
    public static final String OPERATE_TOPIC_BACK = "operate_topic_back";
    /**
     * 测试主题
     */
    public static final String TEST_TOPIC = "test_topic";
    /**
     * 断开链接主题
     */
    public static final String CONNECT_LOST = "connect_lost";
    /**
     * 主题通配下级(所有的)
     */
    public static final String TOPIC_WILDCARD_ALL_LEVEL = "/#";
    /**
     * 主题通配下一级，后面再加就是两级
     */
    public static final String TOPIC_WILDCARD_NEXT_LEVEL = "/+";
    /**
     * mqtt客户端 在线状态码
     */
    public static final String MQTT_CLIENT_ONLINE = "1";
    /**
     * mqtt客户端 离线状态码
     */
    public static final String MQTT_CLIENT_OFFLINE = "2";
    /**
     * mqtt设备 启动状态码
     */
    public static final String MQTT_DEVICE_START = "1";
    /**
     * mqtt设备 停止状态码
     */
    public static final String MQTT_DEVICE_STOP = "2";

    /**
     * 把当前累计的qos转成int数组
     *
     * @return
     */
    public static int[] convertAllQOS() {
        int[] r = new int[ALL_QOS.size()];
        Iterator<Integer> it = ALL_QOS.iterator();
        for (int i = 0; i < r.length; i++) {
            r[i] = it.next();
        }
        return r;
    }

    /**
     * 操作主题转换
     *
     * @return
     */
    public static String operateTopicConvert(String topic, String code) {
        return (ObjectUtils.isEmpty(code)) ? (topic + "/" + OPERATE_TOPIC) : (topic + "/" + code + "/" + OPERATE_TOPIC);
    }

    /**
     * 厂商订阅主题转换
     *
     * @return
     */
    public static String firmSubTopicConvert(String topic) {
        if (topic.equals("#")) {
            return topic;
        } else {
            return topic + TOPIC_WILDCARD_NEXT_LEVEL + TOPIC_WILDCARD_NEXT_LEVEL;
        }
    }

}
