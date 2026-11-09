package com.taotao.cloud.mq.consistency.ttc.remoting.netty;

import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyRemotingAbstract;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RemotingCommand;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * Netty Server Handler
 * 处理入站消息
 * <p>
 * 设计要点：{@code processMessageReceived} 必须调用 {@link NettyRemotingAbstract#processMessageReceived}，
 * 否则 REQUEST / RESPONSE 命令永远不会被分发到 processor / responseTable。
 * 默认实现只 log 一行 — 这是历史上很多 z-mq 自测失败的根本原因。
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<RemotingCommand> {

    private static final Logger log = LoggerFactory.getLogger(NettyServerHandler.class);

    /** 持有 {@link NettyRemotingAbstract} 引用，让其接管真正的 dispatch. */
    private final NettyRemotingAbstract remoting;

    public NettyServerHandler(NettyRemotingAbstract remoting) {
        this.remoting = remoting;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RemotingCommand msg) throws Exception {
        // 交由 NettyRemotingAbstract 真正分发 (REQUEST → processor; RESPONSE → responseFuture)
        this.remoting.processMessageReceived(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Exception caught in NettyServerHandler: {}", cause.getMessage(), cause);
        ctx.close();
    }
}
