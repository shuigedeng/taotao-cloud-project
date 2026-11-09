package com.taotao.cloud.mq.consistency.ttc.common.protocol;

/**
 * 发送状态（对标 RocketMQ SendStatus）.
 * <p>
 * 协议层共享 — Broker 与 Client 都引用同一个枚举，避免 Broker 依赖 Client。
 */
public enum SendStatus {
    /** 发送成功 */
    SEND_OK,
    /** 刷盘超时 */
    FLUSH_DISK_TIMEOUT,
    /** 同步刷盘到 Slave 超时 */
    FLUSH_SLAVE_TIMEOUT,
    /** Slave 不可用 */
    SLAVE_NOT_AVAILABLE,
    /** 通用发送失败（含路由失败、消息非法等） */
    SEND_FAILED,
    /** 路由失败：无可用队列 */
    NO_ROUTE,
    /** 消息非法 */
    MESSAGE_ILLEGAL,
    /** 未知错误 */
    UNKNOWN_ERROR
}
