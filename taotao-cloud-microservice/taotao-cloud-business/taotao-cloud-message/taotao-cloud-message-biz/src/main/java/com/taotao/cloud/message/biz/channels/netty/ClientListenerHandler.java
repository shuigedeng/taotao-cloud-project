package com.taotao.cloud.message.biz.channels.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * ClientListenerHandler
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@Slf4j
@ChannelHandler.Sharable
public class ClientListenerHandler extends SimpleChannelInboundHandler<MessageBean> {

    /**
     * 服务端上线的时候调用
     */
    @Override
    public void channelActive( ChannelHandlerContext ctx ) throws Exception {
        log.info("{}连上了服务器", ctx.channel().remoteAddress());
    }

    /**
     * 服务端掉线的时候调用
     */
    @Override
    public void channelInactive( ChannelHandlerContext ctx ) throws Exception {
        log.info("{}断开了服务器", ctx.channel().remoteAddress());
        ctx.fireChannelInactive();
    }


    /**
     * 读取服务端消息
     */
    @Override
    protected void channelRead0( ChannelHandlerContext channelHandlerContext, MessageBean messageBean )
            throws Exception {
        log.info("来自服务端的消息:{}", new String(messageBean.getContent(), CharsetUtil.UTF_8));
        channelHandlerContext.channel().close();
    }

    /**
     * 异常发生时候调用
     */
    @Override
    public void exceptionCaught( ChannelHandlerContext ctx, Throwable cause ) throws Exception {
        log.error("{}连接出异常了", ctx.channel().remoteAddress());
        log.error(ExceptionUtil.printStackTrace((Exception) cause));
        ctx.close();
    }
}

