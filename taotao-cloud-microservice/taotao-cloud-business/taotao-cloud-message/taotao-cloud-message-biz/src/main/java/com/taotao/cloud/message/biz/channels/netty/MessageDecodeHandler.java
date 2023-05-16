package com.taotao.cloud.message.biz.channels.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MessageDecodeHandler extends ByteToMessageDecoder {


	@Override
	protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
		int len = byteBuf.readInt();
		byte[] content = new byte[len];
		byteBuf.readBytes(content);
		MessageBean messageBean = new MessageBean();
		messageBean.setContent(content);
		messageBean.setLen(len);
		list.add(messageBean);
	}
}

