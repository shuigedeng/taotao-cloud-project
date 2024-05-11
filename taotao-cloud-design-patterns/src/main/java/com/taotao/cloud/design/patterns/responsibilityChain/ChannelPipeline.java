package com.taotao.cloud.design.patterns.responsibilityChain;

/**
 * ChannelPipeline
 *
 */
public interface ChannelPipeline {

    ChannelPipeline process(Object in,
                            Object out);

    ChannelPipeline addLast(String name, ChannelHandler handler);

    Channel channel();

    ChannelPipeline fireExceptionCaught(Throwable cause,
                                        Object in,
                                        Object out);

    ChannelPipeline fireChannelProcess(Object in,
                                       Object out);

    ChannelHandlerContext head();

    ChannelHandlerContext tail();
}
