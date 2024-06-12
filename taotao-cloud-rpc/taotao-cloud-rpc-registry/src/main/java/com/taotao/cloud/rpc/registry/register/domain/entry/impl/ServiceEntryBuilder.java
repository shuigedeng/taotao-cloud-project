
package com.taotao.cloud.rpc.registry.register.domain.entry.impl;


import com.taotao.cloud.rpc.registry.register.domain.entry.ServiceEntry;

/**
 * <p> 服务明细工具类 </p>
 *
 * @since 2024.06
 */
public final class ServiceEntryBuilder {

    private ServiceEntryBuilder(){}

    /**
     * 指定服务标识
     * @param serviceId 服务标识
     * @param ip ip 地址
     * @param port 端口号
     * @return 服务明细
     * @since 2024.06
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
     * @since 2024.06
     */
    public static ServiceEntry of(final String serviceId) {
        return of(serviceId, null, 0);
    }


}
