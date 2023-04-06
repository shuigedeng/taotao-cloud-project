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

package com.taotao.cloud.sys.biz.event.application;

import com.taotao.cloud.common.utils.log.LogUtils;
import org.apache.dubbo.config.spring.context.event.DubboApplicationStateEvent;
import org.apache.dubbo.config.spring.context.event.DubboConfigInitEvent;
import org.apache.dubbo.config.spring.context.event.ServiceBeanExportedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class DubboEventListener {
    @Component
    public static class ServiceBeanExportedEventListener implements ApplicationListener<ServiceBeanExportedEvent> {
        @Override
        public void onApplicationEvent(ServiceBeanExportedEvent event) {
            LogUtils.info("DubboEventListener ----- ServiceBeanExportedEvent onApplicationEvent {}", event);
        }
    }

    @Component
    public static class DubboApplicationStateEventListener implements ApplicationListener<DubboApplicationStateEvent> {
        @Override
        public void onApplicationEvent(DubboApplicationStateEvent event) {
            LogUtils.info("DubboEventListener ----- DubboApplicationStateEvent onApplicationEvent {}", event);
        }
    }

    @Component
    public static class DubboConfigInitEventListener implements ApplicationListener<DubboConfigInitEvent> {
        @Override
        public void onApplicationEvent(DubboConfigInitEvent event) {
            LogUtils.info("DubboEventListener ----- DubboConfigInitEvent onApplicationEvent {}", event);
        }
    }
}
