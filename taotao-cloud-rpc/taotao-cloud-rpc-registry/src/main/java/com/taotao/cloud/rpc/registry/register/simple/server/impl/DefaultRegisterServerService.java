/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.register.simple.server.impl;

import com.github.houbb.heaven.util.common.ArgUtil;
import com.github.houbb.heaven.util.guava.Guavas;
import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.heaven.util.util.CollectionUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.rpc.register.domain.entry.ServiceEntry;
import com.github.houbb.rpc.register.simple.server.RegisterServerService;
import io.netty.channel.Channel;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p> 默认服务注册类 </p>
 *
 * <pre> Created: 2019/10/23 9:16 下午  </pre>
 * <pre> Project: rpc  </pre>
 *
 * @author houbinbin
 * @since 0.0.8
 */
public class DefaultRegisterServerService implements RegisterServerService {

    private static final Log LOG = LogFactory.getLog(DefaultRegisterServerService.class);

    /**
     * 存放对应的 map 信息
     * @since 0.0.8
     */
    private final Map<String, Set<ServiceEntry>> map;

    /**
     * 服务端对应的 channel 信息
     * @since 0.1.8
     */
    private final Map<ServiceEntry, Channel> serviceEntryChannelMap;

    public DefaultRegisterServerService(){
        map = new ConcurrentHashMap<>();
        serviceEntryChannelMap = new ConcurrentHashMap<>();
    }

    @Override
    public List<ServiceEntry> register(ServiceEntry serviceEntry, Channel channel) {
        paramCheck(serviceEntry);

        final String serviceId = serviceEntry.serviceId();
        Set<ServiceEntry> serviceEntrySet = map.get(serviceId);
        if(ObjectUtil.isNull(serviceEntrySet)) {
            serviceEntrySet = Guavas.newHashSet();
        }

        LOG.info("[Register Server] add service: {}", serviceEntry);
        serviceEntrySet.add(serviceEntry);
        map.put(serviceId, serviceEntrySet);

        serviceEntryChannelMap.put(serviceEntry, channel);
        // 返回更新后的结果
        return Guavas.newArrayList(serviceEntrySet);
    }

    @Override
    public List<ServiceEntry> unRegister(ServiceEntry serviceEntry) {
        paramCheck(serviceEntry);

        final String serviceId = serviceEntry.serviceId();
        Set<ServiceEntry> serviceEntrySet = map.get(serviceId);

        if(CollectionUtil.isEmpty(serviceEntrySet)) {
            // 服务列表为空
            LOG.info("[Register Server] remove service set is empty. entry: {}", serviceEntry);
            return Guavas.newArrayList();
        }

        serviceEntrySet.remove(serviceEntry);
        LOG.info("[Register Server] remove service: {}", serviceEntry);
        map.put(serviceId, serviceEntrySet);

        serviceEntryChannelMap.remove(serviceEntry);

        // 返回更新后的结果
        return Guavas.newArrayList(serviceEntrySet);
    }

    @Override
    public List<ServiceEntry> lookUp(String serviceId) {
        ArgUtil.notEmpty(serviceId, "serviceId");

        LOG.info("[Register Server] start lookUp serviceId: {}", serviceId);
        Set<ServiceEntry> serviceEntrySet = map.get(serviceId);
        LOG.info("[Register Server] end lookUp serviceId: {}, list: {}", serviceId,
                serviceEntrySet);
        return Guavas.newArrayList(serviceEntrySet);
    }

    @Override
    public Collection<Channel> channels() {
        return serviceEntryChannelMap.values();
    }

    @Override
    public Collection<ServiceEntry> serviceEntries() {
        return serviceEntryChannelMap.keySet();
    }

    @Override
    public Collection<ServiceEntry> serviceEntries(String ipPort) {
        Collection<ServiceEntry> serviceEntries = serviceEntries();

        Set<ServiceEntry> set = new HashSet<>();
        for(ServiceEntry serviceEntry : serviceEntries) {
            String ip = serviceEntry.ip();
            int port = serviceEntry.port();
            String key = ip+":"+port;

            if(key.equals(ipPort)) {
                set.add(serviceEntry);
            }
        }
        return set;
    }

    /**
     * 参数校验
     * @param serviceEntry 服务明细
     * @since 0.0.8
     */
    private void paramCheck(final ServiceEntry serviceEntry) {
        ArgUtil.notNull(serviceEntry, "serviceEntry");
        final String serviceId = serviceEntry.serviceId();
        ArgUtil.notEmpty(serviceId, "serviceId");
    }

}
