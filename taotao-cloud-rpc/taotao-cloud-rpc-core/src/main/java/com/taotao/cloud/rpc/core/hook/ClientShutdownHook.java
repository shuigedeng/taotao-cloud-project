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

package com.taotao.cloud.rpc.core.hook;

import com.taotao.cloud.rpc.common.factory.ThreadPoolFactory;
import com.taotao.cloud.rpc.core.net.netty.client.ChannelProvider;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户端清除钩子
 */
@Slf4j
public class ClientShutdownHook {

    private static final ClientShutdownHook shutdownHook = new ClientShutdownHook();

    public static ClientShutdownHook getShutdownHook() {
        return shutdownHook;
    }

    /**
     * 添加清除钩子
     */
    public void addClearAllHook() {
        log.info("All services will be cancel after shutdown");
        Runtime.getRuntime()
                .addShutdownHook(
                        new Thread(
                                () -> {
                                    ChannelProvider.shutdownAll();
                                    ThreadPoolFactory.shutdownAll();
                                }));
    }
}
