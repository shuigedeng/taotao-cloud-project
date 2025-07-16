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

package com.taotao.cloud.rpc.common.common.config.registry.impl;

import com.taotao.cloud.rpc.common.common.config.component.Credential;
import com.taotao.cloud.rpc.common.common.config.component.RpcAddress;
import com.taotao.cloud.rpc.common.common.config.registry.RegistryConfig;
import java.util.List;

/**
 * 注册中心配置类
 * @author shuigedeng
 * @since 2024.06
 */
public class DefaultRegistryConfig implements RegistryConfig {

    /**
     * 地址配置列表
     * @since 2024.06
     */
    private List<RpcAddress> rpcAddressList;

    /**
     * 凭证信息
     * @since 2024.06
     */
    private Credential credential;

    @Override
    public List<RpcAddress> addressList() {
        return rpcAddressList;
    }

    public DefaultRegistryConfig addressList(List<RpcAddress> rpcAddressList) {
        this.rpcAddressList = rpcAddressList;
        return this;
    }

    @Override
    public Credential credential() {
        return credential;
    }

    public DefaultRegistryConfig credential(Credential credential) {
        this.credential = credential;
        return this;
    }
}
