/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.rocketmq.configuration;

import com.taotao.cloud.common.factory.YamlPropertySourceFactory;
import com.taotao.cloud.rocketmq.properties.RocketmqProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * RocketMQConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 20:54:47
 */
@Configuration
@EnableConfigurationProperties({RocketmqProperties.class})
@ConditionalOnProperty(prefix = RocketmqProperties.PREFIX, name = "enabled", havingValue = "true")
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:mate-rocketmq.yml")
public class RocketMQConfiguration {

}
