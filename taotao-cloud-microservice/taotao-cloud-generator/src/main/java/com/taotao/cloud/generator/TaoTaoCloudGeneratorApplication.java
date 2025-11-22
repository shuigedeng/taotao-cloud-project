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

package com.taotao.cloud.generator;

import com.taotao.boot.core.startup.StartupSpringApplication;
import com.taotao.boot.webmvc.annotation.TaoTaoBootApplication;
import com.taotao.cloud.generator.maku.GeneratorAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * 代码生成器管理中心
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-05-11 13:55:49
 */
@Import(GeneratorAutoConfiguration.class)
@MapperScan(
        basePackages = {"com.taotao.cloud.generator.maku.dao", "com.taotao.cloud.generator.mapper"})
@ComponentScan(
        basePackages = {"com.taotao.cloud.generator.maku.dao", "com.taotao.cloud.generator.mapper"})
@TaoTaoBootApplication
public class TaoTaoCloudGeneratorApplication {

    public static void main(String[] args) {
        new StartupSpringApplication(TaoTaoCloudGeneratorApplication.class)
                .setTtcBanner()
                .setTtcProfileIfNotExists("dev")
                .setTtcApplicationProperty("taotao-cloud-generator")
                .setTtcAllowBeanDefinitionOverriding(true)
                .run(args);
    }
}
