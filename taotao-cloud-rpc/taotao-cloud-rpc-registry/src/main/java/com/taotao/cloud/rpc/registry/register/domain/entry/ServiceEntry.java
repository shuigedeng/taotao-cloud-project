/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.register.domain.entry;

import java.io.Serializable;

/**
 * <p> 注册服务信息 </p>
 *
 * <pre> Created: 2019/10/23 8:04 下午  </pre>
 * <pre> Project: rpc  </pre>
 *
 * （1）每一个 serviceId 是可以对应多台 ip:port 信息的。
 * @author houbinbin
 * @since 0.0.8
 */
public interface ServiceEntry extends Serializable {

    /**
     * 服务标识
     * @return 服务标识
     * @since 0.0.8
     */
    String serviceId();

    /**
     * 服务描述
     * @return 服务描述
     * @since 0.0.8
     */
    String description();

    /**
     * 机器 ip 信息
     *
     * <pre>
     *     InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
     *     String clientIP = insocket.getAddress().getHostAddress();
     * </pre>
     *
     * @return 机器 ip 信息
     * @since 0.0.8
     */
    String ip();

    /**
     * 端口信息
     * @return 端口信息
     * @since 0.0.8
     */
    int port();

    /**
     * 权重信息
     * @return 权重信息
     * @since 0.0.8
     */
    int weight();

}
