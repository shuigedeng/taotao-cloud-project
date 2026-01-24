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

package com.taotao.cloud.gateway;

import com.taotao.boot.core.startup.StartupSpringApplication;
import com.taotao.boot.security.spring.annotation.EnableOauth2ResourceServer;
import com.taotao.boot.security.spring.annotation.EnableSecurityConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import reactor.netty.ReactorNetty;

/**
 * 网关系统中心
 *
 * <p>http://192.168.218.2:33333/swagger-ui.html</p>
 * <p>http://192.168.218.2:33333/doc.html</p>
 * <p>
 *
 * 启动问题查找 org.springframework.context.support.AbstractApplicationContext.refresh方法
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/10/10 09:06
 */
@EnableOauth2ResourceServer
@EnableSecurityConfiguration
@SpringBootApplication
@EnableDiscoveryClient
public class TaoTaoCloudGatewayWebmvcApplication {

    public static void main(String[] args) {
        System.setProperty("com.google.protobuf.use_unsafe_pre22_gencode", "true");

        // 可以减少connection timed错误 可以提升20%左右的qpc
        System.setProperty(ReactorNetty.IO_SELECT_COUNT, "1");
        // System.setProperty(ReactorNetty.IO_WORKER_COUNT, "1");

        // 要想查看Reactor Netty访问时的日志信息，你必须设置如下系统属性：
        // -Dreactor.netty.http.server.accessLogEnabled=true
        // SpringApplication.run(TaoTaoCloudGatewayApplication.class, args);
        new StartupSpringApplication(TaoTaoCloudGatewayWebmvcApplication.class)
                .setTtcBanner()
                .setTtcProfileIfNotExists("dev")
                .setTtcApplicationProperty("taotao-cloud-gateway")
                //.setTtcAllowBeanDefinitionOverriding(true)
                .run(args);

        // 获取本地 ip 地址
        // String ip = InetAddress.getLocalHost().getHostAddress();
    }
}
