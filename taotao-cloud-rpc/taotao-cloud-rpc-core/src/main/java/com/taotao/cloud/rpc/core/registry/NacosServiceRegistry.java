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

package com.taotao.cloud.rpc.core.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.taotao.cloud.rpc.common.exception.RegisterFailedException;
import com.taotao.cloud.rpc.common.exception.RpcException;
import com.taotao.cloud.rpc.common.util.NacosUtils;

import java.net.InetSocketAddress;

import lombok.extern.slf4j.Slf4j;

/**
 * NacosServiceRegistry
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public class NacosServiceRegistry implements ServiceRegistry {

    /**
     * 以默认组名”DEFAULT_GROUP“, 注册服务名对应的服务套接字地址
     *
     * @param serviceName 服务名
     * @param inetSocketAddress 套接字地址
     */
    @Override
    public void register( String serviceName, InetSocketAddress inetSocketAddress )
            throws RpcException {
        try {
            NacosUtils.registerService(serviceName, inetSocketAddress);
        } catch (NacosException e) {
            log.error("Failed to register service, {}", e.getMessage());
            throw new RegisterFailedException("Failed to register service Exception");
        }
    }

    /**
     * 注册组名下服务名对应的服务套接字地址
     *
     * @param serviceName 服务名
     * @param groupName 组名
     * @param inetSocketAddress 套接字地址
     */
    @Override
    public void register( String serviceName, String groupName, InetSocketAddress inetSocketAddress )
            throws RpcException {
        try {
            NacosUtils.registerService(serviceName, groupName, inetSocketAddress);
        } catch (NacosException e) {
            log.error("Failed to register servicem {}", e.getMessage());
            throw new RegisterFailedException("Failed to register service Exception");
        }
    }
}
