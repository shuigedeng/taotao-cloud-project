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

package com.taotao.cloud.ccsr.server.starter;

import com.taotao.cloud.ccsr.common.config.CcsrConfig;
import com.taotao.cloud.ccsr.core.remote.RpcServer;
import com.taotao.cloud.ccsr.core.remote.raft.RaftServer;
import com.taotao.cloud.ccsr.server.starter.utils.BannerUtils;
import com.taotao.cloud.ccsr.spi.SpiExtensionFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.context.WebApplicationContext;

/**
 * CcsrServerInitializer
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class CcsrServerInitializer implements ApplicationListener<ContextRefreshedEvent>, DisposableBean {

    private final RpcServer rpcServer;

    private final RaftServer raftServer;

    private final BannerUtils bannerUtils;

    private final CcsrConfig config;

    public CcsrServerInitializer( RpcServer rpcServer, CcsrConfig config, BannerUtils bannerUtils ) {
        this.config = config;
        this.rpcServer = rpcServer;
        this.bannerUtils = bannerUtils;
        this.raftServer =
                (RaftServer)
                        SpiExtensionFactory.getExtension(
                                CcsrConfig.RpcType.RAFT.getType(), RpcServer.class);
    }

    @Override
    public void destroy() {
        rpcServer.stop();
        raftServer.stop();
    }

    @Override
    public void onApplicationEvent( ContextRefreshedEvent event ) {
        if (event.getApplicationContext().getParent() == null) {
            ApplicationContext ctx = event.getApplicationContext();
            RpcServer rpcServer = ctx.getBean(RpcServer.class);
            // 启动RPC服务
            rpcServer.start();

            // 启动并注册Raft服务
            startRaftNode();

            // 如果在 SpringBoot Web 容器环境运行，可以不需要让主线程阻塞
            if (!( ctx instanceof WebApplicationContext )) {
                rpcServer.await();
            }
        }
    }

    private void startRaftNode() {
        raftServer.init(config);
        raftServer.start();
        // bannerUtils.print();
    }
}
