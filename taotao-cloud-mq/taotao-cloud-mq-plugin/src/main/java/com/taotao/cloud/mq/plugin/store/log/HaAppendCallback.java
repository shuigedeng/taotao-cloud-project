package com.taotao.cloud.mq.plugin.store.log;

/**
 * HA 同步回调接口（CommitLog.putMessage 成功后调用）.
 * <p>
 * 由 BrokerController 注入, 实现类把消息推送给所有 Slave.
 *
 * <p>典型流程:
 * <ol>
 *   <li>Master Broker CommitLog.putMessage 成功</li>
 *   <li>CommitLog 调用 haCallback.onMessageAppended(offset, body)</li>
 *   <li>BrokerController 的 lambda 把消息转发给 HAService</li>
 *   <li>HAService 异步推送给所有 Slave</li>
 * </ol>
 *
 * @see CommitLog#setHaCallback
 */
public interface HaAppendCallback {
    /**
     * 新消息已写入 CommitLog, HA 服务可以开始推送.
     *
     * @param commitLogOffset 消息的物理 CommitLog offset
     * @param bodyBytes        已编码的消息字节 (含 magic / crc / body 等)
     */
    void onMessageAppended(long commitLogOffset, byte[] bodyBytes);
}
