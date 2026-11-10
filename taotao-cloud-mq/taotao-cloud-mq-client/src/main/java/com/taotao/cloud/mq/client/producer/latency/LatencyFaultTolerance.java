package com.taotao.cloud.mq.client.producer.latency;

import com.taotao.cloud.mq.common.MessageQueue;

import java.util.Set;

/**
 * 延迟故障容错接口（对标 RocketMQ MQFaultStrategy）.
 * <p>
 * Producer 发送消息前，通过此接口判断 Broker 是否可用，并在发送后更新延迟指标。
 * 核心思想:
 * <ol>
 *   <li>发送前: {@link #isAvailable(String)} 检查 Broker 是否在回避期</li>
 *   <li>发送后: {@link #recordLatency(String, long)} 更新延迟并计算回避时长</li>
 *   <li>发送失败: {@link #markFault(String, Throwable)} 标记为不可用</li>
 * </ol>
 */
public interface LatencyFaultTolerance {

    /**
     * Broker 是否可用 (不在回避期内).
     *
     * @param brokerName Broker 名称
     * @return true 表示可用
     */
    boolean isAvailable(String brokerName);

    /**
     * 记录发送延迟，自动计算是否需要回避.
     * <ul>
     *   <li>latency &lt; 500ms → 正常，不回避</li>
     *   <li>500ms ~ 1s → 回避 10s</li>
     *   <li>1s ~ 5s → 回避 30s</li>
     *   <li>5s ~ 10s → 回避 60s</li>
     *   <li>&gt; 10s → 回避 120s</li>
     * </ul>
     *
     * @param brokerName Broker 名称
     * @param latencyMs  发送耗时 (ms)
     */
    void recordLatency(String brokerName, long latencyMs);

    /**
     * 标记 Broker 为不可用 (发送失败/异常).
     * 回避时长默认 30s.
     *
     * @param brokerName Broker 名称
     * @param cause      异常原因 (可为 null)
     */
    void markFault(String brokerName, Throwable cause);

    /**
     * 从候选队列中排除不可用 Broker 对应的队列.
     *
     * @param queues 候选队列
     * @return 过滤后的可用队列
     */
    Set<MessageQueue> filterAvailableQueues(Set<MessageQueue> queues);

    /**
     * 获取 Broker 的故障信息.
     */
    LatencyFaultInfo getFaultInfo(String brokerName);
}
