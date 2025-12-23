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

package com.taotao.cloud.xxljob;

import com.taotao.boot.core.startup.StartupSpringApplication;
//import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * xxljob 定时任务中心
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/11/26 下午7:55
 */
@MapperScan("com.taotao.cloud.xxljob.mapper")
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
//@EnableEncryptableProperties
@EnableDiscoveryClient
@SpringBootApplication
public class TaoTaoCloudXxlJobApplication {

    public static void main(String[] args) {

        System.setProperty("com.google.protobuf.use_unsafe_pre22_gencode", "true");

        new StartupSpringApplication(TaoTaoCloudXxlJobApplication.class)
                .setTtcBanner()
                .setTtcProfileIfNotExists("dev")
                .setTtcApplicationProperty("taotao-cloud-xxljob")
                //.setTtcAllowBeanDefinitionOverriding(true)
                .run(args);
    }
}
