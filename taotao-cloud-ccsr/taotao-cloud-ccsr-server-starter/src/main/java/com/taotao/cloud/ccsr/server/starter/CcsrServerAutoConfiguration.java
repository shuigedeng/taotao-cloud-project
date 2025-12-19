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
import com.taotao.cloud.ccsr.server.starter.utils.BannerUtils;
import com.taotao.cloud.ccsr.spi.SpiExtensionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

/**
 * CcsrServerAutoConfiguration
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
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
    public RpcServer rpcServer( CcsrConfig config ) {
        RpcServer rpcServer =
                SpiExtensionFactory.getExtension(config.getRpcType(), RpcServer.class);
        rpcServer.init(config);
        return rpcServer;
    }

    /**
     * 定义 CcsrServerInitializer Bean
     */
    @Bean
    public CcsrServerInitializer ccsrServerInitializer(
            RpcServer rpcServer, CcsrConfig config, BannerUtils bannerUtils ) {
        return new CcsrServerInitializer(rpcServer, config, bannerUtils);
    }

    /**
     * Banner Logo打印，支持自定义，和SpringBoot Banner用法一样
     */
    @Bean
    public BannerUtils bannerUtils( ResourceLoader resourceLoader ) {
        return new BannerUtils(resourceLoader);
    }
}
