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

package com.taotao.cloud.rpc.core.provider;

import com.taotao.cloud.rpc.common.exception.ServiceNotFoundException;
import com.taotao.cloud.rpc.common.exception.ServiceNotImplException;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;

/**
 * DefaultServiceProvider
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public class DefaultServiceProvider implements ServiceProvider {

    // 存放服务名-服务对象键值对，不需要实例化，保证全局唯一
    private static final Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    // 存放已注册的 服务对象 对应 key值，不需要实例化，保证全局唯一
    private static final Set<String> registeredService = ConcurrentHashMap.newKeySet();

    @Override
    public synchronized <T> void addServiceProvider( T service, String serviceName )
            throws ServiceNotImplException {
        // 服务名
        if (registeredService.contains(serviceName)) {
            return;
        }
        registeredService.add(serviceName);
        // 获取该 对象的 所有接口 对象
        // 将 service 所有暴露的接口名 以键值对 存入 Map（暴露给客户端的只有接口）
        serviceMap.put(serviceName, service);
        log.info(
                "Register service: {} with interface: {} ",
                service.getClass().getInterfaces(),
                serviceName);
    }

    @Override
    public synchronized Object getServiceProvider( String serviceName )
            throws ServiceNotFoundException {
        Object service = serviceMap.get(serviceName);
        log.debug("getServiceProvider - service [{}]", service);
        if (service == null) {
            throw new ServiceNotFoundException("Service Not Found Exception!");
        }
        return service;
    }
}
