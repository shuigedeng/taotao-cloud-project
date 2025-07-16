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

package com.taotao.cloud.rpc.client.model;

import com.taotao.cloud.rpc.common.common.config.component.RpcAddress;
import java.util.List;

/**
 * 客户端查询服务端连接信息配置
 *
 * @since 0.1.8
 */
public class ClientQueryServerChannelConfig {

    /**
     * 服务唯一标识
     */
    private String serviceId;

    /**
     * 服务地址信息 （1）如果不为空，则直接根据地址获取 （2）如果为空，则采用自动发现的方式
     * <p>
     * 如果为 subscribe 可以自动发现，然后填充这个字段信息。
     *
     * @since 2024.06
     */
    private List<RpcAddress> rpcAddresses;

    /**
     * 是否进行订阅模式
     *
     * @since 2024.06
     */
    private boolean subscribe;

    /**
     * 注册中心列表
     *
     * @since 2024.06
     */
    private List<RpcAddress> registerCenterList;

    /**
     * 客户端启动检测
     *
     * @since 0.1.5
     */
    private boolean check;

    public String serviceId() {
        return serviceId;
    }

    public ClientQueryServerChannelConfig serviceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public List<RpcAddress> rpcAddresses() {
        return rpcAddresses;
    }

    public ClientQueryServerChannelConfig rpcAddresses(List<RpcAddress> rpcAddresses) {
        this.rpcAddresses = rpcAddresses;
        return this;
    }

    public boolean subscribe() {
        return subscribe;
    }

    public ClientQueryServerChannelConfig subscribe(boolean subscribe) {
        this.subscribe = subscribe;
        return this;
    }

    public List<RpcAddress> registerCenterList() {
        return registerCenterList;
    }

    public ClientQueryServerChannelConfig registerCenterList(List<RpcAddress> registerCenterList) {
        this.registerCenterList = registerCenterList;
        return this;
    }

    public boolean check() {
        return check;
    }

    public ClientQueryServerChannelConfig check(boolean check) {
        this.check = check;
        return this;
    }
}
