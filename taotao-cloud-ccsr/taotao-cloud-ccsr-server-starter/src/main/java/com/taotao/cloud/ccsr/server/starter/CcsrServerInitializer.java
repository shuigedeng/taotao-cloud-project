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



public class CcsrServerInitializer implements ApplicationListener<ContextRefreshedEvent>, DisposableBean {

    private final RpcServer rpcServer;

    private final RaftServer raftServer;

    private final BannerUtils bannerUtils;

    private final CcsrConfig config;

    public CcsrServerInitializer(RpcServer rpcServer, CcsrConfig config, BannerUtils bannerUtils) {
        this.config = config;
        this.rpcServer = rpcServer;
        this.bannerUtils = bannerUtils;
        this.raftServer = (RaftServer) SpiExtensionFactory.getExtension(CcsrConfig.RpcType.RAFT.getType(), RpcServer.class);
    }

    @Override
    public void destroy() {
        rpcServer.stop();
        raftServer.stop();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            ApplicationContext ctx = event.getApplicationContext();
            RpcServer rpcServer = ctx.getBean(RpcServer.class);
            // 启动RPC服务
            rpcServer.start();

            // 启动并注册Raft服务
            startRaftNode();

            // 如果在 SpringBoot Web 容器环境运行，可以不需要让主线程阻塞
            if (!(ctx instanceof WebApplicationContext)) {
                rpcServer.await();
            }
        }
    }

    private void startRaftNode() {
        raftServer.init(config);
        raftServer.start();
        //bannerUtils.print();
    }

}
