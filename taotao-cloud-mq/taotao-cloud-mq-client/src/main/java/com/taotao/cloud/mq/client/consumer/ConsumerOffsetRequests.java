package com.taotao.cloud.mq.client.consumer;

import com.taotao.cloud.mq.client.MQClientInstance;
import com.taotao.cloud.mq.remoting.exception.RemotingSendRequestException;
import com.taotao.cloud.mq.remoting.netty.RemotingSysResponseCode;
import com.taotao.cloud.mq.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.remoting.protocol.RequestCode;

/**
 * 消费位点提交的线上协议（client 侧）：{@code UPDATE_CONSUMER_OFFSET}(220) 的请求构造与发送。
 * <p>
 * 为什么要有这个共用口：pull 消费者（显式提交）与 push 消费者（listener 返回成功后写穿提交）
 * 提交的是同一件事。两处各写一遍字段名，就等着哪天一侧改字段名而另一侧静默失效 ——
 * 位点提交静默失效的表象是"重启之后全部重放", 和"根本没有持久化"无法区分。
 * <p>
 * 字段名与 broker 侧 {@code PullMessageProcessor.EXT_CONSUMER_GROUP} /
 * {@code ConsumerOffsetProcessor.EXT_OFFSET} 是同一套（由 ConsumerOffsetWiringGuardTest 机检钉住）。
 */
public final class ConsumerOffsetRequests {

    /** 消费组字段名（pull 请求与提交请求共用这一个 key）. */
    public static final String EXT_CONSUMER_GROUP = "consumerGroup";
    /** Topic 字段名. */
    public static final String EXT_TOPIC = "topic";
    /** 队列 ID 字段名. */
    public static final String EXT_QUEUE_ID = "queueId";
    /** 位点字段名. */
    public static final String EXT_OFFSET = "offset";

    /**
     * 提交 RPC 的超时：位点提交是一次内存写（落盘由 broker 的周期 flush 与关机 flush 兑现），
     * 3s 与本仓不带挂起预算的 pull RPC 超时同级。
     */
    public static final long COMMIT_RPC_TIMEOUT_MILLIS = 3000L;

    private ConsumerOffsetRequests() {
        // 工具类
    }

    /**
     * 构造一次位点提交请求：把 {@code (topic, queueId, group) -> offset} 交给 broker。
     *
     * @param topic   Topic
     * @param queueId 队列 ID
     * @param group   消费组（三元组 key 的一部分, 缺失会被 broker 拒掉）
     * @param offset  下次消费的起始位点（= 已消费的最后一条 + 1）
     * @return UPDATE_CONSUMER_OFFSET 请求
     */
    public static RemotingCommand buildUpdateConsumerOffsetRequest(String topic, int queueId, String group,
                                                                  long offset) {
        RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.UPDATE_CONSUMER_OFFSET);
        request.addExtField(EXT_TOPIC, topic);
        request.addExtField(EXT_QUEUE_ID, String.valueOf(queueId));
        request.addExtField(EXT_CONSUMER_GROUP, group);
        request.addExtField(EXT_OFFSET, String.valueOf(offset));
        return request;
    }

    /**
     * 真发一次提交，并把 broker 记下来的位点读回来。
     * <p>
     * <b>不静默</b>：没拿到响应、或 broker 回了非 SUCCESS（缺 group、位点为负、manager 没装配都算），
     * 一律抛 {@link RemotingSendRequestException} 并带上 remark —— 位点提交失败的代价是"重启后重放",
     * 让它悄悄过去比让它响更糟。
     *
     * @param instance   client 实例（提供到 broker 的通道）
     * @param brokerAddr broker 地址
     * @param topic      Topic
     * @param queueId    队列 ID
     * @param group      消费组
     * @param offset     要提交的位点
     * @return broker 侧记下的位点（响应里回读的那个值）
     */
    public static long sendOffsetCommit(MQClientInstance instance, String brokerAddr, String topic, int queueId,
                                        String group, long offset) throws Exception {
        if (instance == null) {
            throw new IllegalStateException("consumer not started");
        }
        RemotingCommand response = instance.invokeSync(brokerAddr,
                buildUpdateConsumerOffsetRequest(topic, queueId, group, offset), COMMIT_RPC_TIMEOUT_MILLIS);
        if (response == null) {
            throw new RemotingSendRequestException("UPDATE_CONSUMER_OFFSET got no response: broker="
                    + brokerAddr + " topic=" + topic + " queueId=" + queueId + " group=" + group);
        }
        if (response.getCode() != RemotingSysResponseCode.SUCCESS) {
            throw new RemotingSendRequestException("UPDATE_CONSUMER_OFFSET rejected: code=" + response.getCode()
                    + " remark=" + response.getRemark() + " topic=" + topic + " queueId=" + queueId
                    + " group=" + group + " offset=" + offset);
        }
        String stored = response.getExtField(EXT_OFFSET);
        if (stored == null || stored.trim().isEmpty()) {
            // broker 认了但没回读值: 以提交的数为准, 但要说出来（老 broker / 处理器忘了回写都会走到这）
            return offset;
        }
        return Long.parseLong(stored.trim());
    }
}
