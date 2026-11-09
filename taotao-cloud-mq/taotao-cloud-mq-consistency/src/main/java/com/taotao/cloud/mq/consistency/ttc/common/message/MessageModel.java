package com.taotao.cloud.mq.consistency.ttc.common.message;

/**
 * 消息消费模式枚举（对标 RocketMQ MessageModel）.
 * <p>
 * 支持两种消费模式：
 * <ul>
 *   <li>{@link #CLUSTERING} — 集群消费（默认）：同一消费者组内多个实例负载均衡，每条消息只被一个实例消费</li>
 *   <li>{@link #BROADCASTING} — 广播消费：同一消费者组内每个实例都收到全量消息</li>
 * </ul>
 */
public enum MessageModel {

    /**
     * 集群消费（默认）.
     * <p>
     * 同一消费者组内的多个实例通过 rebalance 分配队列，每条消息只被一个实例消费。
     * 适用于大多数业务场景，支持水平扩展。
     */
    CLUSTERING("CLUSTERING"),

    /**
     * 广播消费.
     * <p>
     * 同一消费者组内的每个实例都收到全量消息，不进行队列分配。
     * 适用于需要通知所有实例的场景（如配置更新、本地缓存刷新）。
     * <p>
     * 注意：广播模式下，每个实例独立维护 offset，消息不会在实例间负载均衡。
     */
    BROADCASTING("BROADCASTING");

    private final String name;

    MessageModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * 根据字符串值解析 MessageModel，不区分大小写。
     * 无效值返回 {@link #CLUSTERING}（默认）。
     */
    public static MessageModel fromString(String value) {
        if (value == null || value.isEmpty()) {
            return CLUSTERING;
        }
        try {
            return valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return CLUSTERING;
        }
    }
}
