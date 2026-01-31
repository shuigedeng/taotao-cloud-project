package com.taotao.cloud.message.biz.channels.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * HeartBeatHandler
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    public void userEventTriggered( ChannelHandlerContext ctx, Object evt ) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;//强制类型转化
            if (event.state() == IdleState.READER_IDLE) {
                LogUtils.info("进入读空闲......");
            } else if (event.state() == IdleState.WRITER_IDLE) {
                LogUtils.info("进入写空闲......");
            } else if (event.state() == IdleState.ALL_IDLE) {
                LogUtils.info("channel 关闭之前：users 的数量为：" + UserConnectPool.getChannelGroup().size());
                Channel channel = ctx.channel();
                //资源释放
                channel.close();
                LogUtils.info("channel 关闭之后：users 的数量为：" + UserConnectPool.getChannelGroup().size());
            }
        }
    }


}

