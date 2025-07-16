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

package com.taotao.cloud.mq.common.rpc;

import com.taotao.cloud.mq.common.balance.IServer;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class RpcAddress implements IServer {

    /**
     * address 信息
     *
     * @since 2024.05
     */
    private String address;

    /**
     * 端口号
     *
     * @since 2024.05
     */
    private int port;

    /**
     * 权重
     *
     * @since 2024.05
     */
    private int weight;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String url() {
        return this.address + ":" + port;
    }

    @Override
    public int weight() {
        return this.weight;
    }
}
