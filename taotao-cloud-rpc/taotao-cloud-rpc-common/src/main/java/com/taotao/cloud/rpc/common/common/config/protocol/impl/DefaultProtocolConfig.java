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

package com.taotao.cloud.rpc.common.common.config.protocol.impl;

import com.taotao.cloud.rpc.common.common.config.protocol.ProtocolConfig;

/**
 * 协议配置信息
 * @author shuigedeng
 * @since 2024.06
 */
public class DefaultProtocolConfig implements ProtocolConfig {

    /**
     * 名称
     * RPC
     * HTTP
     * HTTPS
     * @since 2024.06
     */
    private String name;

    /**
     * 协议端口号
     */
    private int port;

    @Override
    public String name() {
        return name;
    }

    public DefaultProtocolConfig name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public int port() {
        return port;
    }

    public DefaultProtocolConfig port(int port) {
        this.port = port;
        return this;
    }
}
