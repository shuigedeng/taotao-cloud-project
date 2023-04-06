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
import org.springframework.cloud.function.context.catalog.FunctionCatalogEvent;
import org.springframework.cloud.function.context.catalog.FunctionRegistrationEvent;
import org.springframework.cloud.function.context.catalog.FunctionUnregistrationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class FunctionEventListener {
    @Component
    public static class FunctionCatalogEventListener implements ApplicationListener<FunctionCatalogEvent> {
        @Override
        public void onApplicationEvent(FunctionCatalogEvent event) {
            LogUtils.info("FunctionEventListener ----- FunctionCatalogEvent onApplicationEvent {}", event);
        }
    }

    @Component
    public static class FunctionRegistrationEventListener implements ApplicationListener<FunctionRegistrationEvent> {
        @Override
        public void onApplicationEvent(FunctionRegistrationEvent event) {
            LogUtils.info("FunctionEventListener ----- FunctionRegistrationEvent onApplicationEvent {}", event);
        }
    }

    @Component
    public static class FunctionUnregistrationEventListener
            implements ApplicationListener<FunctionUnregistrationEvent> {
        @Override
        public void onApplicationEvent(FunctionUnregistrationEvent event) {
            LogUtils.info("FunctionEventListener ----- FunctionUnregistrationEvent onApplicationEvent {}", event);
        }
    }
}
