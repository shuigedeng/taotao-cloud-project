/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.rpc.registry.simple.server.impl;

import com.taotao.cloud.rpc.registry.domain.entry.ServiceEntry;
import com.taotao.cloud.rpc.registry.simple.server.RegisterServerService;
import io.netty.channel.Channel;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p> 默认服务注册类 </p>
 * @since 2024.06
 */
public class DefaultRegisterServerService implements RegisterServerService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultRegisterServerService.class);

    /**
     * 存放对应的 map 信息
     * @since 2024.06
     */
    private final Map<String, Set<ServiceEntry>> map;

    /**
     * 服务端对应的 channel 信息
     * @since 0.1.8
     */
    private final Map<ServiceEntry, Channel> serviceEntryChannelMap;

    public DefaultRegisterServerService() {
        map = new ConcurrentHashMap<>();
        serviceEntryChannelMap = new ConcurrentHashMap<>();
    }

    @Override
    public List<ServiceEntry> register(ServiceEntry serviceEntry, Channel channel) {
        paramCheck(serviceEntry);

        final String serviceId = serviceEntry.serviceId();
        Set<ServiceEntry> serviceEntrySet = map.get(serviceId);
        //        if(ObjectUtil.isNull(serviceEntrySet)) {
        //            serviceEntrySet = Guavas.newHashSet();
        //        }

        //        LOG.info("[Register Server] add service: {}", serviceEntry);
        serviceEntrySet.add(serviceEntry);
        map.put(serviceId, serviceEntrySet);

        serviceEntryChannelMap.put(serviceEntry, channel);
        // 返回更新后的结果
        //        return Guavas.newArrayList(serviceEntrySet);
        return null;
    }

    @Override
    public List<ServiceEntry> unRegister(ServiceEntry serviceEntry) {
        paramCheck(serviceEntry);

        final String serviceId = serviceEntry.serviceId();
        Set<ServiceEntry> serviceEntrySet = map.get(serviceId);

        //        if(CollectionUtil.isEmpty(serviceEntrySet)) {
        //            // 服务列表为空
        //            LOG.info("[Register Server] remove service set is empty. entry: {}",
        // serviceEntry);
        //            return Guavas.newArrayList();
        //        }
        //
        //        serviceEntrySet.remove(serviceEntry);
        //        LOG.info("[Register Server] remove service: {}", serviceEntry);
        //        map.put(serviceId, serviceEntrySet);

        serviceEntryChannelMap.remove(serviceEntry);

        // 返回更新后的结果
        //        return Guavas.newArrayList(serviceEntrySet);
        return null;
    }

    @Override
    public List<ServiceEntry> lookUp(String serviceId) {
        //        ArgUtil.notEmpty(serviceId, "serviceId");

        //        LOG.info("[Register Server] start lookUp serviceId: {}", serviceId);
        //        Set<ServiceEntry> serviceEntrySet = map.get(serviceId);
        //        LOG.info("[Register Server] end lookUp serviceId: {}, list: {}", serviceId,
        //                serviceEntrySet);
        //        return Guavas.newArrayList(serviceEntrySet);
        return null;
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
        for (ServiceEntry serviceEntry : serviceEntries) {
            String ip = serviceEntry.ip();
            int port = serviceEntry.port();
            String key = ip + ":" + port;

            if (key.equals(ipPort)) {
                set.add(serviceEntry);
            }
        }
        return set;
    }

    /**
     * 参数校验
     * @param serviceEntry 服务明细
     * @since 2024.06
     */
    private void paramCheck(final ServiceEntry serviceEntry) {
        //        ArgUtil.notNull(serviceEntry, "serviceEntry");
        final String serviceId = serviceEntry.serviceId();
        //        ArgUtil.notEmpty(serviceId, "serviceId");
    }
}
