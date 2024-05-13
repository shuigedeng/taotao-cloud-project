/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.register.simple.client;


import com.github.houbb.rpc.register.domain.entry.ServiceEntry;
import io.netty.channel.Channel;

import java.util.Collection;
import java.util.List;

/**
 * <p> 客户端注册服务类 </p>
 *
 * <pre> Created: 2019/10/23 9:08 下午  </pre>
 * <pre> Project: rpc  </pre>
 *
 * @author houbinbin
 * @since 0.0.8
 */
public interface RegisterClientService {

    /**
     * 监听服务信息
     * （1）监听之后，如果有任何相关的机器信息发生变化，则进行推送。
     * （2）内置的信息，需要传送 ip 信息到注册中心。
     *
     * @param serviceEntry 客户端明细信息
     * @param clientChannel 客户端 channel 信息
     * @since 0.0.8
     */
    void subscribe(final ServiceEntry serviceEntry,
                   final Channel clientChannel);

    /**
     * 取消监听服务信息
     *
     * （1）将改服务从客户端的监听列表中移除即可。
     *
     * @param serviceEntry 客户端明细信息
     * @param clientChannel 客户端 channel 信息
     * @since 0.0.8
     */
    void unSubscribe(final ServiceEntry serviceEntry,
                     final Channel clientChannel);

    /**
     * 注册通知
     *
     * 核心流程：
     * （1）根据 serviceId 直接获取对应的所有 client 列表信息
     * （2）根据 serviceId 获取所有的对应列表
     * （3）循环通知。
     * @param serviceId 服务信息（不可为空）
     * @param serviceEntry 服务明细
     * @since 0.0.8
     */
    void registerNotify(final String serviceId, final ServiceEntry serviceEntry);

    /**
     * 注册通知
     *
     * 核心流程：
     * （1）根据 serviceId 直接获取对应的所有 client 列表信息
     * （2）根据 serviceId 获取所有的对应列表
     * （3）循环通知。
     * @param serviceId 服务信息（不可为空）
     * @param serviceEntry 服务明细
     * @since 0.0.8
     */
    void unRegisterNotify(final String serviceId, final ServiceEntry serviceEntry);

    /**
     * channel 列表
     * @return 列表
     * @since 0.1.8
     */
    Collection<Channel> channels();

}
