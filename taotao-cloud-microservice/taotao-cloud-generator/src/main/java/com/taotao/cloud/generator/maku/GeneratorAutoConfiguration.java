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

package com.taotao.cloud.generator.maku;

import com.taotao.cloud.generator.maku.config.template.GeneratorConfig;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * spring boot starter AutoConfiguration
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Configuration
@AllArgsConstructor
@ComponentScan(basePackages = {"com.taotao.cloud.generator.maku"})
@EnableConfigurationProperties(GeneratorProperties.class)
public class GeneratorAutoConfiguration {
    private final GeneratorProperties properties;

    @Bean
    GeneratorConfig generatorConfig() {
        return new GeneratorConfig(properties.getTemplate());
    }
}
