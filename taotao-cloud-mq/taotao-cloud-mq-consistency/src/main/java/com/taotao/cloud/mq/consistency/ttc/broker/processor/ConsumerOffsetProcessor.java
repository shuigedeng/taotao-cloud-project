package com.taotao.cloud.mq.consistency.ttc.broker.processor;

import com.taotao.cloud.mq.consistency.ttc.broker.BrokerController;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyRemotingAbstract;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.RemotingSysResponseCode;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RequestCode;
import com.taotao.cloud.mq.consistency.ttc.store.config.ConsumerOffsetManager;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * Broker 端"消费位点提交"处理器（对标 RocketMQ ConsumerManageProcessor#updateConsumerOffset）.
 * <p>
 * <b>这一支为什么存在</b>：{@code BrokerController} 的 javadoc 广告"consumerOffsetManager:
 * 消费位点持久化, 跨重启恢复"，而后端（{@link ConsumerOffsetManager} 的
 * {@code commitOffset/queryOffset/flush/load}）是真的、{@code BrokerController} 也确实 start 了它并周期刷盘 ——
 * 但 {@link RequestCode#UPDATE_CONSUMER_OFFSET}(220) 在接上本处理器之前<b>除声明行外全仓零引用</b>，
 * 也就是没有任何人往那儿写。本类把那半边的第一条边补上：请求 → 已经活着的那个 manager。
 * <p>
 * <b>只做提交，不做重置</b>：{@code RESET_CONSUMER_OFFSET}(410) 的语义（回到哪里、要不要丢消息）
 * 还没定，本类收到它就回 {@link RemotingSysResponseCode#REQUEST_CODE_NOT_SUPPORTED}（与
 * {@code AdminBrokerProcessor}/{@code BrokerOutAPI} 对未知码的既有口径一致），不猜。
 * <p>
 * <b>响应形状</b>：不落 body（仓里没有跨 broker/client 的提交结果 DTO，而新增一个要动
 * {@code z-mq-common} 的对外契约），
 * 改为回写 {@code offset} 扩展字段 = <b>从 manager 里读回来的那位</b>。回读而不是把请求里的数原样吐回去，
 * 是为了让客户端（和测试）能看见"broker 真的记下了"这件事，而不是"broker 把我的请求抄了一遍"。
 */
public class ConsumerOffsetProcessor implements NettyRemotingAbstract.NettyRequestProcessor {

    private static final Logger log = LoggerFactory.getLogger(ConsumerOffsetProcessor.class);

    /** 提交请求里的位点字段名（与 pull 请求里的 {@code offset} 同一个线上契约）. */
    public static final String EXT_OFFSET = "offset";

    private final BrokerController brokerController;

    public ConsumerOffsetProcessor(BrokerController brokerController) {
        this.brokerController = brokerController;
    }

    @Override
    public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) throws Exception {
        RemotingCommand response = RemotingCommand.createResponseCommand(RemotingSysResponseCode.SUCCESS);
        response.setOpaque(request.getOpaque());

        switch (request.getCode()) {
            case RequestCode.UPDATE_CONSUMER_OFFSET:
                return handleUpdateConsumerOffset(request, response);
            default:
                response.setCode(RemotingSysResponseCode.REQUEST_CODE_NOT_SUPPORTED);
                response.setRemark("consumer offset code " + request.getCode() + " not supported");
                return response;
        }
    }

    /**
     * 提交一个 (topic, queueId, group) 的消费位点.
     * <p>
     * 校验口径照 {@code SendMessageProcessor}：缺字段回 {@code SYSTEM_ERROR} + 点名 remark；
     * 数值解析不吞异常（本仓 pull 侧对非法 maxNum/offset 也是直接抛出，不静默按 0 处理）。
     */
    private RemotingCommand handleUpdateConsumerOffset(RemotingCommand request, RemotingCommand response) {
        String topic = request.getExtField("topic");
        String queueIdStr = request.getExtField("queueId");
        String group = request.getExtField(PullMessageProcessor.EXT_CONSUMER_GROUP);
        String offsetStr = request.getExtField(EXT_OFFSET);

        if (topic == null || topic.isEmpty()) {
            return reject(response, "topic missing");
        }
        if (queueIdStr == null || queueIdStr.trim().isEmpty()) {
            return reject(response, "queueId missing");
        }
        // group 是这个三元组 key 的一部分; 没有它就写不进"按组恢复"读的那一格
        if (group == null || group.trim().isEmpty()) {
            return reject(response, "consumerGroup missing");
        }
        if (offsetStr == null || offsetStr.trim().isEmpty()) {
            return reject(response, "offset missing");
        }

        int queueId = Integer.parseInt(queueIdStr.trim());
        long offset = Long.parseLong(offsetStr.trim());
        if (queueId < 0) {
            return reject(response, "queueId must be >= 0");
        }
        if (offset < 0) {
            return reject(response, "offset must be >= 0");
        }

        ConsumerOffsetManager manager = brokerController.getConsumerOffsetManager();
        if (manager == null) {
            return reject(response, "consumerOffsetManager not initialized");
        }

        manager.commitOffset(topic, queueId, group.trim(), offset);
        long stored = manager.queryOffset(topic, queueId, group.trim());
        log.info("consumer offset committed: topic={} queueId={} group={} offset={} stored={}",
                topic, queueId, group, offset, stored);

        response.setCode(RemotingSysResponseCode.SUCCESS);
        response.addExtField(EXT_OFFSET, String.valueOf(stored));
        response.addExtField("stored", String.valueOf(stored == offset));
        return response;
    }

    private RemotingCommand reject(RemotingCommand response, String remark) {
        response.setCode(RemotingSysResponseCode.SYSTEM_ERROR);
        response.setRemark(remark);
        log.warn("UPDATE_CONSUMER_OFFSET rejected: {}", remark);
        return response;
    }

    @Override
    public boolean rejectRequest() {
        return false;
    }
}
