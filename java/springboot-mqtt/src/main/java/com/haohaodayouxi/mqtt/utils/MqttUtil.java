package com.haohaodayouxi.mqtt.utils;

import com.haohaodayouxi.mqtt.constants.MqttConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Arrays;

/**
 * MqttUtil
 *
 * @author TONE
 * @date 2024/9/25
 */
@Slf4j
public class MqttUtil {

    /**
     * 生成 MqttClient(自动重连，不清除session)
     *
     * @param callback  回调函数
     * @param id        id
     * @param user      账号
     * @param pwd       密码
     * @param url       mqtt服务器地址 多个情况,分割
     * @param timeOut   超时时间
     * @param keepAlive 会话心跳时间
     * @return
     */
    public static MqttClient baseMqttClient(MqttCallback callback, String id, String user, String pwd, String url, int timeOut, int keepAlive) {
        MqttClient client = baseMqttClient(id, user, pwd, url, timeOut, keepAlive);
        // 设置回调
        client.setCallback(callback);
        return client;
    }

    /**
     * 生成 MqttClient(自动重连，不清除session)
     *
     * @param id        id
     * @param user      账号
     * @param pwd       密码
     * @param url       mqtt服务器地址 多个情况,分割
     * @param timeOut   超时时间
     * @param keepAlive 会话心跳时间
     * @return
     */
    public static MqttClient baseMqttClient(String id, String user, String pwd, String url, int timeOut, int keepAlive) {
        try {
            MqttClient client = new MqttClient(url, id, new MemoryPersistence());
            client.connect(baseMqttConnectOptions(user, pwd, url, timeOut, keepAlive));
            return client;
        } catch (Exception e) {
            log.error("生成MqttClient异常", e);
            throw new RuntimeException("生成MqttClient异常");
        }
    }

    /**
     * 生成 MqttClient
     *
     * @param callback      回调函数
     * @param id            id
     * @param user          账号
     * @param pwd           密码
     * @param url           mqtt服务器地址 多个情况,分割
     * @param timeOut       超时时间
     * @param keepAlive     会话心跳时间
     * @param autoReconnect 重新连接
     * @param clean         清除缓存，需要重新订阅，原订阅会失效
     * @return
     */
    public static MqttClient baseMqttClient(MqttCallback callback, String id, String user, String pwd, String url, int timeOut, int keepAlive, boolean autoReconnect, boolean clean) {
        MqttClient client = baseMqttClient(id, user, pwd, url, timeOut, keepAlive, autoReconnect, clean);
        // 设置回调
        client.setCallback(callback);
        return client;
    }

    /**
     * 生成 MqttClient
     *
     * @param id            id
     * @param user          账号
     * @param pwd           密码
     * @param url           mqtt服务器地址 多个情况,分割
     * @param timeOut       超时时间
     * @param keepAlive     会话心跳时间
     * @param autoReconnect 重新连接
     * @param clean         清除缓存，需要重新订阅，原订阅会失效
     * @return
     */
    public static MqttClient baseMqttClient(String id, String user, String pwd, String url, int timeOut, int keepAlive, boolean autoReconnect, boolean clean) {
        try {
            log.info("clientId = {}", id);
            MqttClient client = new MqttClient(url, id, new MemoryPersistence());
            client.connect(baseMqttConnectOptions(user, pwd, url, timeOut, keepAlive, autoReconnect, clean));
            return client;
        } catch (Exception e) {
            log.error("生成MqttClient异常", e);
            throw new RuntimeException("生成MqttClient异常");
        }
    }

    /**
     * 生成 MqttClient
     *
     * @param callback      回调函数
     * @param id            id
     * @param user          账号
     * @param pwd           密码
     * @param url           mqtt服务器地址 多个情况,分割
     * @param timeOut       超时时间
     * @param keepAlive     会话心跳时间
     * @param autoReconnect 重新连接
     * @param clean         清除缓存，需要重新订阅，原订阅会失效
     * @param willTopic     遗嘱主题
     * @param willData      遗嘱消息
     * @param willQos       遗嘱级别
     * @param willRetained  遗嘱是否持久化
     * @return
     */
    public static MqttClient baseMqttClient(MqttCallback callback, String id, String user, String pwd, String url,
                                            int timeOut, int keepAlive,
                                            boolean autoReconnect, boolean clean,
                                            String willTopic, String willData, int willQos, boolean willRetained) {
        MqttClient client = baseMqttClient(id, user, pwd, url, timeOut, keepAlive, autoReconnect, clean, willTopic, willData, willQos, willRetained);
        // 设置回调
        client.setCallback(callback);
        return client;
    }

    /**
     * 生成 MqttClient
     *
     * @param id            id
     * @param user          账号
     * @param pwd           密码
     * @param url           mqtt服务器地址 多个情况,分割
     * @param timeOut       超时时间
     * @param keepAlive     会话心跳时间
     * @param autoReconnect 重新连接
     * @param clean         清除缓存，需要重新订阅，原订阅会失效
     * @param willTopic     遗嘱主题
     * @param willData      遗嘱消息
     * @param willQos       遗嘱级别
     * @param willRetained  遗嘱是否持久化
     * @return
     */
    public static MqttClient baseMqttClient(String id, String user, String pwd, String url, int timeOut, int keepAlive,
                                            boolean autoReconnect, boolean clean,
                                            String willTopic, String willData, int willQos, boolean willRetained) {
        try {
            MqttClient client = new MqttClient(url, id, new MemoryPersistence());
            client.connect(baseMqttConnectOptions(user, pwd, url, timeOut, keepAlive, autoReconnect, clean, willTopic, willData, willQos, willRetained));
            return client;
        } catch (Exception e) {
            log.error("生成MqttClient异常", e);
            throw new RuntimeException("生成MqttClient异常");
        }
    }

    /**
     * 生成 MqttConnectOptions(自动重连，不清除session)
     *
     * @param user      账号
     * @param pwd       密码
     * @param url       mqtt服务器地址 多个情况,分割
     * @param timeOut   超时时间
     * @param keepAlive 会话心跳时间
     * @return MqttConnectOptions
     */
    public static MqttConnectOptions baseMqttConnectOptions(String user, String pwd, String url, int timeOut, int keepAlive) {
        return baseMqttConnectOptions(user, pwd, url, timeOut, keepAlive, true, false, MqttConstant.WILL_TOPIC, MqttConstant.WILL_DATA, MqttConstant.QOS_2, false);
    }

    /**
     * 生成 MqttConnectOptions
     *
     * @param user      账号
     * @param pwd       密码
     * @param url       mqtt服务器地址 多个情况,分割
     * @param timeOut   超时时间
     * @param keepAlive 会话心跳时间
     * @param clean     清除缓存，需要重新订阅，原订阅会失效
     * @return MqttConnectOptions
     */
    public static MqttConnectOptions baseMqttConnectOptions(String user, String pwd, String url, int timeOut, int keepAlive, boolean autoReconnect, boolean clean) {
        return baseMqttConnectOptions(user, pwd, url, timeOut, keepAlive, autoReconnect, clean, MqttConstant.WILL_TOPIC, MqttConstant.WILL_DATA, MqttConstant.QOS_2, false);
    }

    /**
     * 生成 MqttConnectOptions
     *
     * @param user          账号
     * @param pwd           密码
     * @param url           mqtt服务器地址 多个情况,分割
     * @param timeOut       超时时间
     * @param keepAlive     会话心跳时间
     * @param autoReconnect 断线重连
     * @param clean         清除缓存，需要重新订阅，原订阅会失效
     * @param willTopic     遗嘱主题
     * @param willData      遗嘱消息
     * @param willQos       遗嘱级别
     * @param willRetained  遗嘱是否持久化
     * @return
     */
    public static MqttConnectOptions baseMqttConnectOptions(String user, String pwd, String url, int timeOut, int keepAlive,
                                                            boolean autoReconnect, boolean clean,
                                                            String willTopic, String willData, int willQos, boolean willRetained) {
        MqttConnectOptions options = new MqttConnectOptions();
        // 设置连接的用户名
        options.setUserName(user);
        // 设置连接的密码
        options.setPassword(pwd.toCharArray());
        options.setServerURIs(StringUtils.split(url, ","));
        // 设置超时时间 单位为秒
        options.setConnectionTimeout(timeOut);
        // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送心跳判断客户端是否在线，但这个方法并没有重连的机制
        options.setKeepAliveInterval(keepAlive);
        //设置断开后重新连接
        options.setAutomaticReconnect(autoReconnect);
        if (ObjectUtils.isNotEmpty(willTopic) && ObjectUtils.isNotEmpty(willData)) {
            // 设置“遗嘱”消息的话题，若客户端与服务器之间的连接意外中断，服务器将发布客户端的“遗嘱”消息。
            //options.setWill(willTopic, willData.getBytes(), willQos, willRetained);
        }
        // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，
        // 这里设置为true表示每次连接到服务器都以新的身份连接 false反正
        options.setCleanSession(clean);
        return options;
    }

    /**
     * 订阅某一个主题 ，此方法默认的的Qos等级为：1
     *
     * @param client 客户端
     * @param topic  订阅的主题
     */
    public static boolean subscribe(MqttClient client, String topic) {
        return baseSubscribe(client, topic, MqttConstant.QOS_1);
    }

    /**
     * 订阅某些主题 ，此方法默认的的Qos等级为：1
     *
     * @param client 客户端
     * @param topic  订阅的主题
     */
    public static boolean subscribe(MqttClient client, String[] topic) {
        return baseSubscribe(client, topic);
    }


    /**
     * 基础订阅方法
     *
     * @param client 客户端
     * @param topic  订阅的主题
     * @param qos    消息质量：0、1、2
     */
    public static boolean baseSubscribe(MqttClient client, String[] topic, int[] qos) {
        try {
            if (null == client || !client.isConnected()) {
                log.error("=======客户端未连接=======");
                return false;
            }
            log.debug("=======尝试订阅,topic:{},qos:{}=======", Arrays.toString(topic), Arrays.toString(qos));
            if (ObjectUtils.isEmpty(qos)) {
                client.subscribe(topic);
            } else {
                if (topic.length != qos.length) {
                    log.error("=======订阅主题和qos长度不一致=======");
                    return false;
                }
                client.subscribe(topic, qos);
            }
            log.debug("=======订阅结束=======");
            return true;
        } catch (MqttException e) {
            log.error("=======订阅异常=======", e);
        }
        return false;
    }

    public static boolean baseSubscribe(MqttClient client, String topic, int qos) {
        return baseSubscribe(client, new String[]{topic}, new int[]{qos});
    }

    public static boolean baseSubscribe(MqttClient client, String[] topic) {
        return baseSubscribe(client, topic, null);
    }

    /**
     * 基础取消订阅方法
     *
     * @param client 客户端
     * @param topic  取消订阅的主题
     */
    public static boolean baseUnSubscribe(MqttClient client, String[] topic) {
        try {
            if (null == client || !client.isConnected()) {
                return false;
            }
            log.debug("=======尝试取消订阅,topic:{}=======", Arrays.toString(topic));
            client.unsubscribe(topic);
            log.debug("=======取消订阅结束=======");
            return true;
        } catch (MqttException e) {
            log.error("=======取消订阅异常=======", e);
        }
        return false;
    }

    public static boolean baseUnSubscribe(MqttClient client, String topic) {
        return baseUnSubscribe(client, new String[]{topic});
    }

    /**
     * 下发消息
     *
     * @param client
     * @param message
     */
    public static boolean publish(MqttClient client, String topic, MqttMessage message) {
        return basePublish(client.getTopic(topic), message);
    }

    /**
     * 下发消息
     *
     * @param client
     * @param topic
     * @param msg
     * @param qos
     * @param retained
     */
    public static boolean basePublish(MqttClient client, String topic, String msg, int qos, boolean retained) {
        return basePublish(client.getTopic(topic), baseMessage(msg, qos, retained));
    }

    /**
     * 下发消息
     *
     * @param topic
     * @param message
     */
    public static boolean basePublish(MqttTopic topic, MqttMessage message) {
        try {
            if (null == topic || null == message) {
                return false;
            }
            log.debug("=======下发消息,topic:{},msg:{}=======", topic, message.toString());
            MqttDeliveryToken token = topic.publish(message);
            token.waitForCompletion();
            log.debug("=======下发消息:{}=======", token.isComplete());
            return token.isComplete();
        } catch (MqttException e) {
            log.error("=======下发消息异常=======", e);
        }
        return false;
    }

    /**
     * 创建 级别为1的非持久化消息对象
     *
     * @param body 消息
     * @return 消息对象
     */
    public static MqttMessage getUnRetainedMessage(String body) {
        return baseMessage(body, MqttConstant.QOS_1, false);
    }

    /**
     * 创建 级别为1的非持久化消息对象
     *
     * @param body 消息
     * @return 消息对象
     */
    public static MqttMessage getRetainedMessage(String body) {
        return baseMessage(body, MqttConstant.QOS_1, true);
    }

    /**
     * 创建 级别为1的消息对象
     *
     * @param body 消息
     * @param qos  消息级别
     * @return 消息对象
     */
    public static MqttMessage getUnRetainedMessage(String body, Integer qos) {
        return baseMessage(body, qos, false);
    }

    /**
     * 创建 级别为1的消息对象
     *
     * @param body     消息
     * @param retained 是否持久化
     * @return 消息对象
     */
    public static MqttMessage getMessage(String body, Boolean retained) {
        return baseMessage(body, MqttConstant.QOS_1, retained);
    }

    /**
     * 创建 级别为1的消息对象
     *
     * @param body 消息
     * @param qos  消息级别
     * @return 消息对象
     */
    public static MqttMessage getRetainedMessage(String body, Integer qos) {
        return baseMessage(body, qos, true);
    }

    /**
     * 基础创建 消息对象
     *
     * @param body     消息
     * @param qos      消息级别
     * @param retained 是否持久化
     * @return 消息对象
     */
    public static MqttMessage baseMessage(String body, Integer qos, Boolean retained) {
        MqttMessage message = new MqttMessage();
        message.setPayload(body.getBytes());
        message.setQos(qos);
        message.setRetained(retained);
        return message;
    }

    /**
     * 关闭MQTT连接
     *
     * @param client 客户端
     */
    public static void close(MqttClient client) {
        try {
            if (null != client && !(client.isConnected())) {
                log.debug("=======尝试关闭=======");
                client.close();
                client.disconnect();
            }
        } catch (MqttException e) {
            log.error("=======尝试关闭异常=======", e);
        }
    }

    /**
     * 重新连接
     *
     * @param client
     */
    public static void reConnect(MqttClient client) {
        try {
            if (null != client && !(client.isConnected())) {
                log.debug("=======重新连接=======");
                client.reconnect();
            }
        } catch (MqttException e) {
            log.error("=======重新连接异常=======", e);
        }
    }


}
