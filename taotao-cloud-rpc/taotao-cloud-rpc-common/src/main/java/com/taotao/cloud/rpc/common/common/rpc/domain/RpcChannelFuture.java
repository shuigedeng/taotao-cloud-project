
package com.taotao.cloud.rpc.common.common.rpc.domain;

import com.taotao.cloud.rpc.common.common.api.Destroyable;
import com.taotao.cloud.rpc.common.common.config.component.RpcAddress;

import io.netty.channel.ChannelFuture;

/**
 * <p> rpc channel future 接口</p>
 * @since 2024.06
 */
public interface RpcChannelFuture extends IServer {

    /**
     * channel future 信息
     * @return ChannelFuture
     * @since 2024.06
     */
    ChannelFuture channelFuture();

    /**
     * 对应的地址信息
     * @return 地址信息
     * @since 2024.06
     */
    RpcAddress address();

    /**
     * 可销毁的对象
     * @return 可销毁的信息
     * @since 0.1.3
     */
    Destroyable destroyable();

}
