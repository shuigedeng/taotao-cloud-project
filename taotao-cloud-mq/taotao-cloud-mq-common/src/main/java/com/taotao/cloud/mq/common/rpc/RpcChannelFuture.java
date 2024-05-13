package com.taotao.cloud.mq.common.rpc;

import io.netty.channel.ChannelFuture;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class RpcChannelFuture extends RpcAddress {

    /**
     * channel future 信息
     * @since 2024.05
     */
    private ChannelFuture channelFuture;

    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }

    public void setChannelFuture(ChannelFuture channelFuture) {
        this.channelFuture = channelFuture;
    }

}
