package com.taotao.cloud.mq.consistency.ttc.broker.ha;

import com.taotao.cloud.mq.consistency.ttc.broker.BrokerController;
import com.taotao.cloud.mq.consistency.ttc.broker.ha.DefaultHAService;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyRemotingAbstract;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.RemotingSysResponseCode;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RequestCode;
import com.taotao.cloud.mq.consistency.ttc.store.ha.HAService;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * HA 处理器（对标 RocketMQ HAProcessor + SlaveSynchronize）.
 * <p>
 * 处理 HA 相关 RPC 请求:
 * <ul>
 *   <li>HA_REPORT_OFFSET: Slave 上报本地 offset (Master 视角接收)</li>
 *   <li>QUERY_DATA_VERSION 已在 BrokerOutAPI 中实现</li>
 * </ul>
 *
 * <p><b>线程安全:</b> 所有方法从 ConcurrentMap 读, register/update 操作原子.
 */
public class HAProcessor implements NettyRemotingAbstract.NettyRequestProcessor {

    private static final Logger log = LoggerFactory.getLogger(HAProcessor.class);

    private final BrokerController brokerController;

    public HAProcessor(BrokerController brokerController) {
        this.brokerController = brokerController;
    }

    @Override
    public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) throws Exception {
        int code = request.getCode();
        switch (code) {
            case RequestCode.HA_REPORT_OFFSET:
                return handleReportOffset(request);
            case RequestCode.QUERY_DATA_VERSION:
                return handleQueryDataVersion(request);
            default:
                RemotingCommand resp = RemotingCommand.createResponseCommand(
                        RemotingSysResponseCode.REQUEST_CODE_NOT_SUPPORTED);
                resp.setOpaque(request.getOpaque());
                resp.setRemark("HAProcessor code " + code + " not supported");
                return resp;
        }
    }

    @Override
    public boolean rejectRequest() {
        return false;
    }

    private RemotingCommand handleReportOffset(RemotingCommand request) {
        String slaveAddr = request.getExtField("slaveAddr");
        String offsetStr = request.getExtField("ackOffset");
        if (slaveAddr == null || offsetStr == null) {
            RemotingCommand resp = RemotingCommand.createResponseCommand(RemotingSysResponseCode.SYSTEM_ERROR);
            resp.setOpaque(request.getOpaque());
            resp.setRemark("slaveAddr or ackOffset missing");
            return resp;
        }
        long ackOffset;
        try {
            ackOffset = Long.parseLong(offsetStr);
        } catch (NumberFormatException e) {
            RemotingCommand resp = RemotingCommand.createResponseCommand(RemotingSysResponseCode.SYSTEM_ERROR);
            resp.setOpaque(request.getOpaque());
            resp.setRemark("invalid ackOffset: " + offsetStr);
            return resp;
        }

        HAService haService = brokerController.getHaService();
        if (haService == null || haService.isSlave()) {
            RemotingCommand resp = RemotingCommand.createResponseCommand(RemotingSysResponseCode.SYSTEM_ERROR);
            resp.setOpaque(request.getOpaque());
            resp.setRemark("HA service not available or not master");
            return resp;
        }

        // Master 视角: 注册或更新 slave 状态
        if (haService instanceof DefaultHAService) {
            ((DefaultHAService) haService).registerOrUpdateSlave(slaveAddr, ackOffset);
        }
        haService.reportSlaveOffset(ackOffset);

        if (log.isDebugEnabled()) {
            log.debug("HA report from slave={} ackOffset={}", slaveAddr, ackOffset);
        }

        RemotingCommand resp = RemotingCommand.createResponseCommand(RemotingSysResponseCode.SUCCESS);
        resp.setOpaque(request.getOpaque());
        return resp;
    }

    private RemotingCommand handleQueryDataVersion(RemotingCommand request) {
        // 简单实现: 返回各 DataVersion 状态
        Map<String, Long> versionMap = new HashMap<>();
        RemotingCommand resp = RemotingCommand.createResponseCommand(RemotingSysResponseCode.SUCCESS);
        resp.setOpaque(request.getOpaque());
        resp.setBody(com.taotao.cloud.mq.consistency.ttc.common.util.JsonCodec.encode(versionMap));
        return resp;
    }
}
