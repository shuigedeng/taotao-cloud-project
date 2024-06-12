package com.taotao.cloud.rpc.server.server.handler;

import com.taotao.cloud.rpc.common.common.config.component.RpcAddress;
import com.taotao.cloud.rpc.registry.register.domain.message.NotifyMessage;
import com.taotao.cloud.rpc.registry.register.domain.message.body.RegisterCenterAddNotifyBody;
import com.taotao.cloud.rpc.registry.register.domain.message.body.RegisterCenterRemoveNotifyBody;
import com.taotao.cloud.rpc.registry.register.domain.message.impl.NotifyMessages;
import com.taotao.cloud.rpc.registry.register.simple.constant.MessageTypeConst;
import com.taotao.cloud.rpc.server.server.support.register.ServerRegisterManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 注册中心
 * （1）用于和注册中心建立长连接。
 * （2）初期设计中服务端不需要做什么事情。
 *
 * 后期可以调整为接收到影响为准，保证请求成功。
 * @author shuigedeng
 * @since 2024.06
 */
public class RpcServerRegisterHandler extends SimpleChannelInboundHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RpcServerRegisterHandler.class);

    private final ServerRegisterManager serverRegisterManager;

    public RpcServerRegisterHandler(ServerRegisterManager serverRegisterManager) {
        this.serverRegisterManager = serverRegisterManager;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
//        LOG.info("[Rpc Server] received message: {}", msg);

        // 分类处理
        NotifyMessage notifyMessage = (NotifyMessage) msg;
        Object body = notifyMessage.body();
        String type = NotifyMessages.type(notifyMessage);
        String seqId = notifyMessage.seqId();
//        LOG.info("[Server Register Listener] received message type: {}, seqId: {} ", type,
//                seqId);

        final Channel channel = ctx.channel();

        switch (type) {
            // 注册中心添加机器
            case MessageTypeConst.REGISTER_CENTER_ADD_NOTIFY:
                RegisterCenterAddNotifyBody addNotifyBody = (RegisterCenterAddNotifyBody) body;
                registerCenterAddNotify(addNotifyBody, channel);
                break;

            // 注册中心移除机器
            case MessageTypeConst.REGISTER_CENTER_REMOVE_NOTIFY:
                RegisterCenterRemoveNotifyBody removeNotifyBody = (RegisterCenterRemoveNotifyBody) body;
                registerCenterRemoveNotify(removeNotifyBody);
                break;


            default:
//                LOG.warn("[Server Register Listener] not support type: {} and seqId: {}",
//                        type, seqId);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.error("[Rpc Server] meet ex", cause);
        ctx.close();
    }


    /**
     * 注册中心添加通知
     * @param addNotifyBody 通知内容
     * @param channel channel
     */
    private void registerCenterAddNotify(RegisterCenterAddNotifyBody addNotifyBody,
                                         final Channel channel) {
//        LOG.info("注册中心添加机器通知：{}", addNotifyBody);
        RpcAddress rpcAddress = new RpcAddress(addNotifyBody.ip(), addNotifyBody.port());
        serverRegisterManager.addRegisterChannel(rpcAddress, channel);
    }

    private void registerCenterRemoveNotify(RegisterCenterRemoveNotifyBody removeNotifyBody) {
//        LOG.info("注册中心移除机器通知：{}", removeNotifyBody);
        RpcAddress rpcAddress = new RpcAddress(removeNotifyBody.ip(), removeNotifyBody.port());
        serverRegisterManager.removeRegisterChannel(rpcAddress);
    }

}
