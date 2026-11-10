package com.taotao.cloud.mq.client.producer;

import com.taotao.cloud.mq.common.enums.TransactionState;
import com.taotao.cloud.mq.common.message.Message;

/**
 * 事务监听器接口（对标 RocketMQ TransactionListener）.
 * <p>
 * 实现此接口以提供本地事务执行和事务状态检查能力：
 * <ul>
 *   <li>{@link #executeLocalTransaction(Message, Object)} — 执行本地事务</li>
 *   <li>{@link #checkLocalTransaction(Message)} — 回查本地事务状态</li>
 * </ul>
 */
public interface TransactionListener {

    /**
     * 执行本地事务.
     * <p>
     * 在发送 Half 消息成功后，Broker 返回 ACK 后调用此方法执行本地事务。
     * <p>
     * 返回值：
     * <ul>
     *   <li>{@link TransactionState#COMMIT} — 本地事务提交成功</li>
     *   <li>{@link TransactionState#ROLLBACK} — 本地事务回滚</li>
     *   <li>{@link TransactionState#UNKNOWN} — 本地事务状态未知，等待 Broker 回查</li>
     * </ul>
     *
     * @param halfMessage Half 消息（已发送到 Broker）
     * @param arg         用户自定义参数（从 sendMessageInTransaction 传入）
     * @return 事务状态
     */
    TransactionState executeLocalTransaction(Message halfMessage, Object arg);

    /**
     * 回查本地事务状态.
     * <p>
     * 当 Broker 未收到二次确认（Commit/Rollback）或收到 UNKNOWN 状态时，
     * 会定期调用此方法检查本地事务的最终状态。
     * <p>
     * 实现此方法时，应根据业务 ID（如订单号）查询数据库确认事务是否提交。
     *
     * @param halfMessage Half 消息
     * @return 事务状态（COMMIT / ROLLBACK / UNKNOWN）
     */
    TransactionState checkLocalTransaction(Message halfMessage);
}
