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

package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * This is Description
 *
 * @since 2022/05/03
 */
@Configuration
public class SpringShutdownHook {

    private static final Logger logger = LoggerFactory.getLogger(SpringShutdownHook.class);

    @Resource
    private ConfigurableApplicationContext configurableApplicationContext;

    @Value("${dubbo.service.shutdown.wait:10000}")
    private static int SHUTDOWN_TIMEOUT_WAIT;

    public SpringShutdownHook() {}

    @PostConstruct
    public void registerShutdownHook() {
        logger.info("[SpringShutdownHook] Register ShutdownHook....");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                int timeOut = SHUTDOWN_TIMEOUT_WAIT;
                SpringShutdownHook.logger.info(
                        "[SpringShutdownHook] Application need sleep {}" + " seconds to wait Dubbo shutdown",
                        timeOut / 1000.0D);
                Thread.sleep((long) timeOut);
                SpringShutdownHook.this.configurableApplicationContext.close();
                SpringShutdownHook.logger.info(
                        "[SpringShutdownHook] ApplicationContext closed," + " Application shutdown");
            } catch (InterruptedException var2) {
                SpringShutdownHook.logger.error(var2.getMessage(), var2);
            }
        }));
    }
}
