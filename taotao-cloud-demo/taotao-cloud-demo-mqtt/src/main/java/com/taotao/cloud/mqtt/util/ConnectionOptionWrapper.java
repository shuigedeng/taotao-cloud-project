package com.taotao.cloud.mqtt.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import static org.eclipse.paho.client.mqttv3.MqttConnectOptions.MQTT_VERSION_3_1_1;

/**
 * 工具类：负责封装 MQ4IOT 客户端的初始化参数设置
 */
public class ConnectionOptionWrapper {
    /**
     * 内部连接参数
     */
    private MqttConnectOptions mqttConnectOptions;
    /**
     * MQ4IOT 实例 ID，购买后控制台获取
     */
    private String instanceId;
    /**
     * 账号 accesskey，从账号系统控制台获取
     */
    private String accessKey;
    /**
     * 账号 secretKey，从账号系统控制台获取，仅在Signature鉴权模式下需要设置
     */
    private String secretKey;
    /**
     * MQ4IOT clientId，由业务系统分配，需要保证每个 tcp 连接都不一样，保证全局唯一，如果不同的客户端对象（tcp 连接）使用了相同的 clientId 会导致连接异常断开。
     * clientId 由两部分组成，格式为 GroupID@@@DeviceId，其中 groupId 在 MQ4IOT 控制台申请，DeviceId 由业务方自己设置，clientId 总长度不得超过64个字符。
     */
    private String clientId;
    /**
     * 客户端使用的 Token 参数，仅在 Token 鉴权模式下需要设置，Key 为 token 类型，一个客户端最多存在三种类型，R，W，RW，Value 是 token内容。
     * 应用需要保证 token 在过期及时更新。否则会导致连接异常。
     */
    private Map<String, String> tokenData = new ConcurrentHashMap<String, String>();

    /**
     * Token 鉴权模式下构造方法
     *
     * @param instanceId MQ4IOT 实例 ID，购买后控制台获取
     * @param accessKey 账号 accesskey，从账号系统控制台获取
     * @param clientId MQ4IOT clientId，由业务系统分配
     * @param tokenData 客户端使用的 Token 参数，仅在 Token 鉴权模式下需要设置
     */
    public ConnectionOptionWrapper(String instanceId, String accessKey, String clientId,
        Map<String, String> tokenData) {
        this.instanceId = instanceId;
        this.accessKey = accessKey;
        this.clientId = clientId;
        if (tokenData != null) {
            this.tokenData.putAll(tokenData);
        }
        mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName("Token|" + accessKey + "|" + instanceId);
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : tokenData.entrySet()) {
            builder.append(entry.getKey()).append("|").append(entry.getValue()).append("|");
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        mqttConnectOptions.setPassword(builder.toString().toCharArray());
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setKeepAliveInterval(90);
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setMqttVersion(MQTT_VERSION_3_1_1);
        mqttConnectOptions.setConnectionTimeout(5000);
    }

    /**
     * Signature 鉴权模式下构造方法
     *
     * @param instanceId MQ4IOT 实例 ID，购买后控制台获取
     * @param accessKey 账号 accesskey，从账号系统控制台获取
     * @param clientId MQ4IOT clientId，由业务系统分配
     * @param secretKey 账号 secretKey，从账号系统控制台获取
     */
    public ConnectionOptionWrapper(String instanceId, String accessKey, String secretKey,
        String clientId) throws NoSuchAlgorithmException, InvalidKeyException {
        this.instanceId = instanceId;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.clientId = clientId;
        mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName("Signature|" + accessKey + "|" + instanceId);
        mqttConnectOptions.setPassword(Tools.macSignature(clientId, secretKey).toCharArray());
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setKeepAliveInterval(90);
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setMqttVersion(MQTT_VERSION_3_1_1);
        mqttConnectOptions.setConnectionTimeout(5000);
    }

    public MqttConnectOptions getMqttConnectOptions() {
        return mqttConnectOptions;
    }

}
