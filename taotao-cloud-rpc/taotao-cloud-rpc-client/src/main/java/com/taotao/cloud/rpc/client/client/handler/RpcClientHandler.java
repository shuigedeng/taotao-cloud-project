
package com.taotao.cloud.rpc.client.client.handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.taotao.cloud.rpc.common.common.support.invoke.InvokeManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.taotao.cloud.rpc.common.common.rpc.domain.RpcResponse;
/**
 * <p> 客户端处理类 </p>
 * @since 2024.06
 */
@ChannelHandler.Sharable
public class RpcClientHandler extends SimpleChannelInboundHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RpcClientHandler.class);

    /**
     * 调用服务管理类
     *
     * @since 2024.06
     */
    private final InvokeManager invokeManager;

    public RpcClientHandler(InvokeManager invokeManager) {
        this.invokeManager = invokeManager;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcResponse rpcResponse = (RpcResponse)msg;
        invokeManager.addResponse(rpcResponse.seqId(), rpcResponse);
//        log.info("[Client] server response is :{}", rpcResponse);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		LOG.error("[Rpc Client] meet ex ", cause);
        ctx.close();
    }

}
