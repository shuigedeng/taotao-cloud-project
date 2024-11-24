package com.taotao.cloud.netty.itcast.test;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoop;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class TestChannel {
    public static void main(String[] args) throws IOException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        EmbeddedChannel c1 = new EmbeddedChannel(false,false,
                new ChannelInboundHandlerAdapter(){
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        log.debug("{}",msg);
                        super.channelRead(ctx, msg);
                    }
                }
        );
        EmbeddedChannel c2 = new EmbeddedChannel(
                new ChannelInboundHandlerAdapter(){
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        log.debug("{}",msg);
                        super.channelRead(ctx, msg);
                    }
                }
        );
        EmbeddedChannel c3 = new EmbeddedChannel(
                new ChannelInboundHandlerAdapter(){
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        log.debug("{}",msg);
                        super.channelRead(ctx, msg);
                    }
                }
        );

        group.next().register(c1);
        group.next().register(c2);
        group.next().register(c3);
        c1.writeInbound("1");
        c2.writeInbound("2");
        c3.writeInbound("3");
        c1.writeInbound("1");
        c2.writeInbound("2");
        c3.writeInbound("3");
        System.in.read();
    }
}
