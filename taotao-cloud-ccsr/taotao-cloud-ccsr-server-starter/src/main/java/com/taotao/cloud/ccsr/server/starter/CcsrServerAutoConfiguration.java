package com.taotao.cloud.ccsr.server.starter;

import com.taotao.cloud.ccsr.common.config.CcsrConfig;
import com.taotao.cloud.ccsr.core.remote.RpcServer;
import com.taotao.cloud.ccsr.server.starter.utils.BannerUtils;
import com.taotao.cloud.ccsr.spi.SpiExtensionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;


@Configuration // 这里加不加这个注解都不影响，只是表示他是一个配置类
@Conditional(EnableCcsrCondition.class) // 核心控制
public class CcsrServerAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "ccsr")
    public CcsrConfig ccsrConfig() {
        return new CcsrConfig();
    }

    @Bean(name = "rpcServer")
    @ConditionalOnProperty(value = "ccsr.rpc-type")
    public RpcServer rpcServer(CcsrConfig config) {
        RpcServer rpcServer = SpiExtensionFactory.getExtension(config.getRpcType(), RpcServer.class);
        rpcServer.init(config);
        return rpcServer;
    }

    /**
     * 定义 CcsrServerInitializer Bean
     */
    @Bean
    public CcsrServerInitializer ccsrServerInitializer(RpcServer rpcServer, CcsrConfig config, BannerUtils bannerUtils) {
        return new CcsrServerInitializer(rpcServer, config, bannerUtils);
    }

    /**
     * Banner Logo打印，支持自定义，和SpringBoot Banner用法一样
     * @param resourceLoader
     * @return
     */
    @Bean
    public BannerUtils bannerUtils(ResourceLoader resourceLoader) {
        return new BannerUtils(resourceLoader);
    }

}
