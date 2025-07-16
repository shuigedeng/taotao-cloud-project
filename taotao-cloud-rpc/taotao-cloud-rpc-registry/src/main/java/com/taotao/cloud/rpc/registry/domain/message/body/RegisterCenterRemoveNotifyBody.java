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

package com.taotao.cloud.rpc.registry.domain.message.body;

import java.io.Serializable;

/**
 * 注册中心移除通知对象
 * @since 0.1.8
 */
public class RegisterCenterRemoveNotifyBody implements Serializable {

    /**
     * 机器 ip 信息
     * @since 0.1.8
     */
    private String ip;

    /**
     * 端口信息
     * @since 0.1.8
     */
    private int port;

    public String ip() {
        return ip;
    }

    public RegisterCenterRemoveNotifyBody ip(String ip) {
        this.ip = ip;
        return this;
    }

    public int port() {
        return port;
    }

    public RegisterCenterRemoveNotifyBody port(int port) {
        this.port = port;
        return this;
    }

    @Override
    public String toString() {
        return "RegisterCenterAddNotifyBody{" + "ip='" + ip + '\'' + ", port=" + port + '}';
    }
}
