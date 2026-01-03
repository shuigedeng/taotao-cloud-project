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
import com.taotao.cloud.rpc.common.idworker.utils.JRedisHelper;
import com.taotao.cloud.rpc.common.util.IpUtils;
import com.taotao.cloud.rpc.common.util.NacosUtils;
import com.taotao.cloud.rpc.core.net.netty.server.NettyServer;
import lombok.extern.slf4j.Slf4j;

/**
 * ServerShutdownHook
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public class ServerShutdownHook {

    private static final ServerShutdownHook shutdownHook = new ServerShutdownHook();

    public static ServerShutdownHook getShutdownHook() {
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
                                    JRedisHelper.remWorkerId(IpUtils.getPubIpAddr());
                                    log.info("the cache for workId has bean cleared successfully");
                                    NacosUtils.clearRegistry();
                                    NettyServer.shutdownAll();
                                    ThreadPoolFactory.shutdownAll();
                                }));
    }
}
