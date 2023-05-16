package com.taotao.cloud.message.biz.channels.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class ClientHandler extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new MessageEncodeHandler());
        pipeline.addLast(new MessageDecodeHandler());
        pipeline.addLast(new ClientListenerHandler());
    }
}

