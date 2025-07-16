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

package com.taotao.cloud.ccsr.client.starter;

import com.taotao.cloud.ccsr.client.client.CcsrClient;
import com.taotao.cloud.ccsr.client.option.GrpcOption;
import com.taotao.cloud.ccsr.common.config.CcsrConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@Conditional(EnableCcsrCondition.class) // 核心控制
public class CcsrClientAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "ccsr")
    public CcsrConfig ccsrConfig() {
        return new CcsrConfig();
    }

    @Bean
    @Scope("singleton") // 显式声明单例
    public CcsrService ccsrClient(CcsrConfig config) {
        GrpcOption option = new GrpcOption();
        // 初始化服务端集群地址
        option.initServers(config.getClusterAddress());
        // 构建ccsrClient
        CcsrClient ccsrClient = CcsrClient.builder(config.getNamespace(), option).build();
        return new CcsrService(ccsrClient);
    }
}
