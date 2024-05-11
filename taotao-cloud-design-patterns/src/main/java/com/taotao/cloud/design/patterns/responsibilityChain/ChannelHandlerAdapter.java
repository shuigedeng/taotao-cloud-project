package com.taotao.cloud.design.patterns.responsibilityChain;

public abstract class ChannelHandlerAdapter implements ChannelHandler {

    @Override
    public void channelProcess(ChannelHandlerContext ctx, Object in, Object out) throws Exception {
        ctx.fireChannelProcess(in, out);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause, Object in, Object out) throws Exception {
        ctx.fireExceptionCaught(cause, in, out);
    }
}
