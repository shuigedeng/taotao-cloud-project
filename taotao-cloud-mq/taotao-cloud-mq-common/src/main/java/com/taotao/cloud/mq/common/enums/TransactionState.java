package com.taotao.cloud.mq.common.enums;

/**
 * 事务状态枚举（对标 RocketMQ TransactionState）.
 * <p>
 * 用于事务消息的二次确认：
 * <ul>
 *   <li>{@link #COMMIT} — 提交事务，消息对消费者可见</li>
 *   <li>{@link #ROLLBACK} — 回滚事务，消息被丢弃</li>
 *   <li>{@link #UNKNOWN} — 未知状态，等待 Broker 回查</li>
 * </ul>
 */
public enum TransactionState {

    /**
     * 提交事务.
     * <p>
     * 本地事务执行成功，Broker 将 Half 消息提交到原始 Topic，消息对消费者可见。
     */
    COMMIT,

    /**
     * 回滚事务.
     * <p>
     * 本地事务执行失败或需要回滚，Broker 丢弃 Half 消息。
     */
    ROLLBACK,

    /**
     * 未知状态.
     * <p>
     * 无法确定本地事务结果（如网络超时），等待 Broker 发起回查。
     */
    UNKNOWN
}
