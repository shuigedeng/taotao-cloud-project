
package com.taotao.cloud.rpc.registry.register.simple.handler;

import com.taotao.cloud.rpc.registry.register.domain.entry.ServiceEntry;
import com.taotao.cloud.rpc.registry.register.domain.message.NotifyMessage;
import com.taotao.cloud.rpc.registry.register.domain.message.body.ServerHeartbeatBody;
import com.taotao.cloud.rpc.registry.register.domain.message.impl.NotifyMessages;
import com.taotao.cloud.rpc.registry.register.simple.SimpleRpcRegister;
import com.taotao.cloud.rpc.registry.register.simple.client.RegisterClientService;
import com.taotao.cloud.rpc.registry.register.simple.client.impl.DefaultRegisterClientService;
import com.taotao.cloud.rpc.registry.register.simple.constant.MessageTypeConst;
import com.taotao.cloud.rpc.registry.register.simple.server.RegisterServerService;
import com.taotao.cloud.rpc.registry.register.simple.server.impl.DefaultRegisterServerService;
import com.taotao.cloud.rpc.registry.register.spi.RpcRegister;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * <p> 注册中心服务器处理类 </p>
 *
 * <pre> Created: 2019/10/23 10:29 下午  </pre>
 * <pre> Project: rpc  </pre>
 * <p>
 * 请求的标准化：
 * （1）对于 server 的服务注册，client 的配置拉取。
 * 二者都是将 register 作为服务端。所以需要统一请求信息。
 * （2）对于 server 的注册，不需要提供对应的反馈信息
 * （3）当配置发生变化时，需要及时通知所有的 client 端。
 * 这里就需要知道哪些是客户端？？
 *
 * @since 2024.06
 */
@ChannelHandler.Sharable
public class RegisterCenterServerHandler extends SimpleChannelInboundHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RegisterCenterServerHandler.class);

    /**
     * 注册中心服务
     * @since 2024.06
     */
    private final RpcRegister rpcRegister;

    public RegisterCenterServerHandler() {
        this.rpcRegister = this.buildSimpleRpcRegister();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        final String id = ctx.channel().id().asLongText();
        LOG.info("[Register Server] channel {} connected " + id);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        NotifyMessage notifyMessage = (NotifyMessage) msg;
        Object body = notifyMessage.body();
        String type = NotifyMessages.type(notifyMessage);
        String seqId = notifyMessage.seqId();
//        LOG.info("[Register Server] received message type: {}, seqId: {} ", type,
//                seqId);

        final Channel channel = ctx.channel();

        switch (type) {
            case MessageTypeConst.SERVER_REGISTER_REQ:
                rpcRegister.register((ServiceEntry)body, channel);
                break;

            case MessageTypeConst.SERVER_UN_REGISTER_REQ:
                rpcRegister.unRegister((ServiceEntry)body);
                break;

            case MessageTypeConst.CLIENT_SUBSCRIBE_REQ:
                rpcRegister.subscribe((ServiceEntry)body, channel);
                break;

            case MessageTypeConst.CLIENT_UN_SUBSCRIBE_REQ:
                rpcRegister.unSubscribe((ServiceEntry)body, channel);
                break;

            case MessageTypeConst.CLIENT_LOOK_UP_SERVER_REQ:
                rpcRegister.lookUp(seqId, (ServiceEntry)body, channel);
                break;

            case MessageTypeConst.SERVER_HEARTBEAT_REQ:
                ServerHeartbeatBody heartbeatBody = (ServerHeartbeatBody) body;
                rpcRegister.serverHeartbeat(heartbeatBody, channel);
                break;

            default:
//                LOG.warn("[Register Center] not support type: {} and seqId: {}",
//                        type, seqId);
        }

    }

    /**
     * 构建简单注册实现类
     * @return 注册实现
     * @since 2024.06
     */
    private RpcRegister buildSimpleRpcRegister() {
        final RegisterServerService registerServerService = new DefaultRegisterServerService();
        final RegisterClientService registerClientService = new DefaultRegisterClientService();
        return new SimpleRpcRegister(registerServerService, registerClientService);
    }



}
