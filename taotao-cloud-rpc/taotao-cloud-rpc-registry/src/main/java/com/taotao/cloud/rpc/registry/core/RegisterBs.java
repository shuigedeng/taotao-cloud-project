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

package com.taotao.cloud.rpc.registry.core;

import com.taotao.cloud.rpc.common.common.remote.netty.handler.ChannelHandlers;
import com.taotao.cloud.rpc.common.common.remote.netty.impl.DefaultNettyServer;
import com.taotao.cloud.rpc.registry.api.config.RegisterConfig;
import com.taotao.cloud.rpc.registry.simple.client.RegisterClientService;
import com.taotao.cloud.rpc.registry.simple.client.impl.DefaultRegisterClientService;
import com.taotao.cloud.rpc.registry.simple.handler.RegisterCenterServerHandler;
import com.taotao.cloud.rpc.registry.simple.server.RegisterServerService;
import com.taotao.cloud.rpc.registry.simple.server.impl.DefaultRegisterServerService;
import io.netty.channel.ChannelHandler;

/**
 * 默认注册中心配置
 * @author shuigedeng
 * @since 2024.06
 */
public class RegisterBs implements RegisterConfig {

    /**
     * 服务启动端口信息
     * @since 2024.06
     */
    private int port;

    /**
     * 服务端
     */
    private final RegisterServerService registerServerService;

    /**
     * 客户端
     */
    private final RegisterClientService registerClientService;

    private RegisterBs() {
        registerServerService = new DefaultRegisterServerService();
        registerClientService = new DefaultRegisterClientService();
    }

    public static RegisterBs newInstance() {
        RegisterBs registerBs = new RegisterBs();
        registerBs.port(8527);
        return registerBs;
    }

    @Override
    public RegisterBs port(int port) {
        //        ArgUtil.notNegative(port, "port");

        this.port = port;
        return this;
    }

    @Override
    public RegisterBs start() {
        ChannelHandler channelHandler =
                ChannelHandlers.objectCodecHandler(new RegisterCenterServerHandler());
        DefaultNettyServer.newInstance(port, channelHandler).asyncRun();

        // 通知对应的服务端和客户端，服务启动。
        // 新增的时候暂时不处理。
        // 暂时注册中心的是无状态的，无法获取到没有访问过的节点。（如果访问过，则客户端肯定已经有对应的信息。）
        // 如果使用 redis/database 等集中式的存储，或者进行数据同步，则有通知的必要性。

        // 添加对应的 shutdown hook
        //        Runtime.getRuntime().addShutdownHook(new Thread() {
        //            @Override
        //            public void run() {
        //                new
        // com.taotao.cloud.rpc.registry.register.support.hook.RegisterCenterShutdownHook(registerServerService, registerClientService, port).hook();
        //            }
        //        });

        return this;
    }
}
