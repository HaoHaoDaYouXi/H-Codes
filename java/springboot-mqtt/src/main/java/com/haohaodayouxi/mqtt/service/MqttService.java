package com.haohaodayouxi.mqtt.service;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

/**
 * RocketMqSendService
 *
 * @author lenovo
 * @date 2022/3/18
 */
public interface MqttService {

    /**
     * 发送消息 (消息质量 默认为1,非持久化的)
     *
     * @param topic 主题(厂商的主题)
     * @param code  设备编号(不传时，请主题内自己拼接上)
     * @param msg   消息
     */
    void pushMsg(@NotBlank(message = "主题不能为空") String topic, String code, String msg);

    /**
     * 发送消息 (非持久化的)
     *
     * @param topic 主题(厂商的主题)
     * @param code  设备编号(不传时，请主题内自己拼接上)
     * @param msg   消息
     * @param qos   消息质量：0、1、2
     */
    void pushMsg(@NotBlank(message = "主题不能为空") String topic, String code, String msg, int qos);

    /**
     * 发送消息 (消息质量 默认为1)
     *
     * @param topic    主题(厂商的主题)
     * @param code     设备编号(不传时，请主题内自己拼接上)
     * @param msg      消息
     * @param retained 是否持久化(每个主题只能存在一条持久化的数据)
     */
    void pushMsg(@NotBlank(message = "主题不能为空") String topic, String code, String msg, boolean retained);

    /**
     * 发送消息
     *
     * @param topic    主题(厂商的主题)
     * @param code     设备编号(不传时，请主题内自己拼接上)
     * @param msg      消息
     * @param qos      消息质量：0、1、2
     * @param retained 是否持久化(每个主题只能存在一条持久化的数据)
     */
    void pushMsg(@NotBlank(message = "主题不能为空") String topic, String code, String msg, int qos, boolean retained);

    /**
     * 下发消息到指定地址 (消息质量 默认为1 非持久化的)
     *
     * @param url   地址
     * @param user  账号
     * @param pwd   密码
     * @param topic 主题
     * @param msg   消息
     */
    void pushMsgByClient(@NotBlank String url, @NotBlank String user, @NotBlank String pwd, @NotBlank String topic, String msg);

    /**
     * 下发消息到指定地址 (非持久化的)
     *
     * @param url   地址
     * @param user  账号
     * @param pwd   密码
     * @param topic 主题
     * @param msg   消息
     * @param qos   消息质量：0、1、2
     */
    void pushMsgByClient(@NotBlank String url, @NotBlank String user, @NotBlank String pwd, @NotBlank String topic, String msg, int qos);

    /**
     * 下发消息到指定地址 (消息质量 默认为1)
     *
     * @param url      地址
     * @param user     账号
     * @param pwd      密码
     * @param topic    主题
     * @param msg      消息
     * @param retained 是否持久化(每个主题只能存在一条持久化的数据)
     */
    void pushMsgByClient(@NotBlank String url, @NotBlank String user, @NotBlank String pwd, @NotBlank String topic, String msg, boolean retained);

    /**
     * 下发消息到指定地址
     *
     * @param url      地址
     * @param user     账号
     * @param pwd      密码
     * @param topic    主题
     * @param msg      消息
     * @param qos      消息质量：0、1、2
     * @param retained 是否持久化(每个主题只能存在一条持久化的数据)
     */
    void pushMsgByClient(@NotBlank String url, @NotBlank String user, @NotBlank String pwd, @NotBlank String topic, String msg, int qos, boolean retained);

    /**
     * 追加订阅主题
     *
     * @param topic 主题
     */
    void appendSubTopic(@NotEmpty String... topic);

    /**
     * 订阅主题(会覆盖之前订阅的主题)
     *
     * @param topic 主题
     */
    void subTopic(@NotEmpty String... topic);

    /**
     * 取消订阅主题
     *
     * @param topic 主题
     */
    void unSubTopic(@NotEmpty String... topic);

    /**
     * 消费消息
     *
     * @param topic
     * @param msg
     */
    void consumeMsg(@NotBlank String topic, String msg);
}
