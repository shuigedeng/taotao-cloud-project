package com.taotao.cloud.iot.biz.enums;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.taotao.cloud.iot.biz.dto.DeviceClientDTO;

import java.util.Arrays;
import java.util.List;


/**
 * 设备消息主题类型枚举
 *
 * @author 
 */
@Getter
@RequiredArgsConstructor
public enum DeviceTopicEnum {
    /**
     * 设备指令下发主题
     */
    COMMAND("command"),

    /**
     * 设备信息上报主题(指令响应反馈)
     */
    COMMAND_RESPONSE("command_response"),

    /**
     * 设备信息上报主题(属性相关)
     */
    PROPERTY("property");

    /**
     * 设备信息上报主题前缀
     */
    public static final String TOPIC_PREFIX = "/iot/device";


    /**
     * MQTT 主题模板，参数：前缀/clientid(deviceType_tenantId_deviceId_uid)/设备主题
     */
    private static final String TOPIC_TEMPLATE = "{}/{}/{}";

    /**
     * 设备主题通配符模板，参数：前缀/+/设备主题
     */
    private static final String TOPIC_WILDCARD_TEMPLATE = "{}/+/{}";

    /**
     * MQTT 主题
     */
    private final String topic;

    /**
     * 获取设备主题通配符
     *
     * @return 主题通配符
     */
    public String getWildcard() {
        return StrUtil.format(TOPIC_WILDCARD_TEMPLATE, TOPIC_PREFIX, getTopic());
    }

    /**
     * 构建完整路径的设备主题
     *
     * @param deviceClient 设备客户端信息
     * @return 完整路径的设备主题
     */
    public String buildTopic(DeviceClientDTO deviceClient) {
        return StrUtil.format(TOPIC_TEMPLATE, TOPIC_PREFIX, deviceClient.buildClientId(), getTopic());
    }

    /**
     * 解析指定的主题字符串为 DeviceTopicEnum 枚举，若无效则抛出 IllegalArgumentException 异常
     *
     * @param topic 主题字符串
     * @return 对应的 DeviceTopicEnum 枚举
     * @throws IllegalArgumentException 当主题字符串无效时抛出
     */
    public static DeviceTopicEnum parse(String topic) {
        String topicSuffix = StrUtil.startWith(topic, TOPIC_PREFIX) ? StrUtil.subAfter(topic, "/", true) : topic;
        return Arrays.stream(DeviceTopicEnum.values())
                .filter(d -> d.name().equalsIgnoreCase(topicSuffix)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(StrUtil.format("无效的 DeviceTopicEnum 值：{}", topic)));
    }

    /**
     * 判断指定的主题是否以给定前缀开头
     *
     * @param topic  主题字符串
     * @param prefix 前缀字符串
     * @return 若主题以给定前缀开头则返回 true，否则返回 false
     */
    public static boolean startsWith(String topic, String prefix) {
        try {
            DeviceTopicEnum deviceTopic = parse(topic);
            return deviceTopic.getTopic().startsWith(prefix);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 解析主题并获取其完整信息（设备信息及主题）
     *
     * @param topic 主题字符串
     * @return 包含设备信息及主题的 DeviceTopicContext 对象
     */
    public static DeviceTopicContext parseContext(String topic) {
        String topicSuffix = StrUtil.subAfter(topic, TOPIC_PREFIX, false);
        List<String> parts = StrUtil.split(topicSuffix, "/", true, true);
        Assert.isTrue(parts.size() == 2, "无效的设备主题:{}", topic);

        DeviceClientDTO deviceClient = DeviceClientDTO.parse(parts.get(0));
        DeviceTopicEnum deviceTopic = DeviceTopicEnum.parse(parts.get(1));

        return DeviceTopicContext.builder()
                .client(deviceClient)
                .topic(deviceTopic)
                .build();
    }

    @Data
    @Accessors(chain=true)
    public static class DeviceTopicContext {
        /**
         * 设备客户端信息
         */
        private DeviceClientDTO client;

        /**
         * 设备主题
         */
        private DeviceTopicEnum topic;
    }
}
