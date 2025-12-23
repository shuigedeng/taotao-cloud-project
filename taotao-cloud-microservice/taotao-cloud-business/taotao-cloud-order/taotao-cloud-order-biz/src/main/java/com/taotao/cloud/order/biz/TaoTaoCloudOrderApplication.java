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

package com.taotao.cloud.order.biz;

import com.taotao.boot.common.utils.common.PropertyUtils;
import com.taotao.boot.core.startup.StartupSpringApplication;
import com.taotao.boot.web.annotation.TaoTaoBootApplication;
import com.taotao.cloud.bootstrap.annotation.TaoTaoCloudApplication;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;

/**
 * --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.lang.reflect=ALL-UNNAMED
 * --add-opens java.base/java.util=ALL-UNNAMED --add-opens
 * jdk.management/com.sun.management.internal=ALL-UNNAMED --add-opens
 * java.base/java.math=ALL-UNNAMED
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:48:21
 */
// @EnableBinding({TaoTaoCloudSink.class, TaoTaoCloudSource.class})
@TaoTaoBootApplication
@TaoTaoCloudApplication
public class TaoTaoCloudOrderApplication {

    public static void main(String[] args) {
		new StartupSpringApplication(TaoTaoCloudOrderApplication.class)
			.setTtcBanner()
			.setTtcProfileIfNotExists("dev")
			.setTtcApplicationProperty("taotao-cloud-order")
			//.setTtcAllowBeanDefinitionOverriding(true)
			.run(args);
    }

    @Bean
    public TopicExchange topicExchange001() {
        return new TopicExchange("sms-topic", true, false);
    }
}
