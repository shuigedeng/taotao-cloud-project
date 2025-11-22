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

package com.taotao.cloud.bff;

import com.taotao.boot.core.startup.StartupSpringApplication;
import com.taotao.boot.webmvc.annotation.TaoTaoBootApplication;
import com.taotao.cloud.bootstrap.annotation.TaoTaoCloudApplication;

// @EnableTaoTaoCloudJpa
// @EnableTaoTaoCloudP6spy
// @EnableTaoTaoCloudFeign
// @EnableTaoTaoCloudLogger
// @EnableTaoTaoCloudSeata
// @EnableTaoTaoCloudSentinel
// @EnableEncryptableProperties
// @EnableTransactionManagement(proxyTargetClass = true)
// @EnableDiscoveryClient
@TaoTaoBootApplication
@TaoTaoCloudApplication
public class TaoTaoCloudBffApplication {

    public static void main(String[] args) {

        new StartupSpringApplication(TaoTaoCloudBffApplication.class)
                .setTtcBanner()
                .setTtcProfileIfNotExists("dev")
                .setTtcApplicationProperty("taotao-cloud-bff")
                .setTtcAllowBeanDefinitionOverriding(true)
                .run(args);
    }
}
