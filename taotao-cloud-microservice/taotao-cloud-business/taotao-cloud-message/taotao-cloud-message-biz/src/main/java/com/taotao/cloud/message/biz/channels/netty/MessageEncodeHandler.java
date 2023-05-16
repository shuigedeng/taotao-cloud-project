package com.taotao.cloud.message.biz.channels.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncodeHandler extends MessageToByteEncoder<MessageBean> {

	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, MessageBean messageBean, ByteBuf byteBuf) throws Exception {
		byteBuf.writeInt(messageBean.getLen());
		byteBuf.writeBytes(messageBean.getContent());
	}
}

