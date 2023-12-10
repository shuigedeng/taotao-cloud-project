package com.taotao.cloud.design.patterns.responsibilityChain;

/**
 * DefaultChannelHandlerContext默认实现
 *
 */
public class DefaultChannelHandlerContext extends AbstractChannelHandlerContext {

    private final ChannelHandler handler;

    DefaultChannelHandlerContext(DefaultChannelPipeline pipeline, String name, ChannelHandler handler) {
        super(pipeline, name, handler.getClass());
        this.handler = handler;
    }

    @Override
    public ChannelHandler handler() {
        return handler;
    }
}
