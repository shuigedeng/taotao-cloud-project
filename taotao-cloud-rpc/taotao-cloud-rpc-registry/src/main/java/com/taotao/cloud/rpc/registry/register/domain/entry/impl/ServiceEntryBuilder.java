/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.register.domain.entry.impl;


import com.github.houbb.rpc.register.domain.entry.ServiceEntry;

/**
 * <p> 服务明细工具类 </p>
 *
 * <pre> Created: 2019/10/23 8:26 下午  </pre>
 * <pre> Project: rpc  </pre>
 *
 * @author houbinbin
 * @since 0.0.8
 */
public final class ServiceEntryBuilder {

    private ServiceEntryBuilder(){}

    /**
     * 指定服务标识
     * @param serviceId 服务标识
     * @param ip ip 地址
     * @param port 端口号
     * @return 服务明细
     * @since 0.0.8
     */
    public static ServiceEntry of(final String serviceId,
                                  final String ip,
                                  final int port) {
        DefaultServiceEntry entry = new DefaultServiceEntry();
        entry.serviceId(serviceId).port(port).ip(ip);
        return entry;
    }

    /**
     * 指定服务标识
     * @param serviceId 服务标识
     * @return 服务明细
     * @since 0.0.8
     */
    public static ServiceEntry of(final String serviceId) {
        return of(serviceId, null, 0);
    }


}
