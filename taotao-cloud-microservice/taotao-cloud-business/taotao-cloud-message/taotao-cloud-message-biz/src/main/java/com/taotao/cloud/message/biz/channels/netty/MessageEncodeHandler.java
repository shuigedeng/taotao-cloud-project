package com.taotao.cloud.message.biz.channels.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * MessageEncodeHandler
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class MessageEncodeHandler extends MessageToByteEncoder<MessageBean> {

    @Override
    protected void encode( ChannelHandlerContext channelHandlerContext, MessageBean messageBean, ByteBuf byteBuf )
            throws Exception {
        byteBuf.writeInt(messageBean.getLen());
        byteBuf.writeBytes(messageBean.getContent());
    }
}

