package com.taotao.cloud.design.patterns.responsibilityChain;

/**
 * ChannelHandler
 *
 */
public interface ChannelHandler {

    void channelProcess(ChannelHandlerContext ctx,
                        Object in,
                        Object out) throws Exception;

    void exceptionCaught(ChannelHandlerContext ctx,
                         Throwable cause,
                         Object in,
                         Object out) throws Exception;
}
