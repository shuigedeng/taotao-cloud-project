package com.taotao.cloud.ccsr.server.starter;

import org.ohara.msc.common.config.OHaraMcsConfig;
import com.taotao.cloud.ccsr.core.remote.RpcServer;
import com.taotao.cloud.ccsr.server.utils.BannerUtils;
import com.taotao.cloud.ccsr.spi.SpiExtensionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;


@Configuration // 这里加不加这个注解都不影响，只是表示他是一个配置类
@Conditional(EnableOHaraMcsCondition.class) // 核心控制
public class OHaraMcsServerAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "ohara-mcs")
    public OHaraMcsConfig oHaraMcsConfig() {
        return new OHaraMcsConfig();
    }

    @Bean(name = "rpcServer")
    @ConditionalOnProperty(value = "ohara-mcs.rpc-type")
    public RpcServer rpcServer(OHaraMcsConfig config) {
        RpcServer rpcServer = SpiExtensionFactory.getExtension(config.getRpcType(), RpcServer.class);
        rpcServer.init(config);
        return rpcServer;
    }

    /**
     * 定义 OHaraMccServerInitializer Bean
     */
    @Bean
    public OHaraMcsServerInitializer oHaraMcsServerInitializer(RpcServer rpcServer, OHaraMcsConfig config, BannerUtils bannerUtils) {
        return new OHaraMcsServerInitializer(rpcServer, config, bannerUtils);
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
