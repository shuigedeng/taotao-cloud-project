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

package com.taotao.cloud.netty.atguigu.netty.dubborpc.provider;

import com.taotao.cloud.netty.atguigu.netty.dubborpc.netty.NettyServer;

// ServerBootstrap 会启动一个服务提供者，就是 NettyServer
/**
 * ServerBootstrap
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class ServerBootstrap {

    public static void main( String[] args ) {

        // 代码代填..
        NettyServer.startServer("127.0.0.1", 7000);
    }
}
