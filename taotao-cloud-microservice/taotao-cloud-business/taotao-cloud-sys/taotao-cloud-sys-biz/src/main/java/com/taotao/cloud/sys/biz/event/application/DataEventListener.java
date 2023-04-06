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
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mapping.context.MappingContextEvent;
import org.springframework.data.relational.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.relational.core.mapping.event.AfterSaveEvent;
import org.springframework.data.relational.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.relational.core.mapping.event.BeforeDeleteEvent;
import org.springframework.data.relational.core.mapping.event.BeforeSaveEvent;
import org.springframework.data.relational.core.mapping.event.RelationalDeleteEvent;
import org.springframework.data.relational.core.mapping.event.RelationalEventWithEntity;
import org.springframework.data.relational.core.mapping.event.RelationalSaveEvent;
import org.springframework.data.repository.init.RepositoriesPopulatedEvent;
import org.springframework.stereotype.Component;

@Configuration
public class DataEventListener {

    @Component
    public static class RepositoriesPopulatedEventListener implements ApplicationListener<RepositoriesPopulatedEvent> {
        @Override
        public void onApplicationEvent(RepositoriesPopulatedEvent event) {
            LogUtils.info("DataEventListener ----- RepositoriesPopulatedEvent onApplicationEvent {}", event);
        }
    }

    @Component
    public static class RelationalSaveEventListener implements ApplicationListener<RelationalSaveEvent> {
        @Override
        public void onApplicationEvent(RelationalSaveEvent event) {
            LogUtils.info("DataEventListener ----- RelationalSaveEvent onApplicationEvent {}", event);
        }
    }

    @Component
    public static class RelationalDeleteEventListener implements ApplicationListener<RelationalDeleteEvent> {
        @Override
        public void onApplicationEvent(RelationalDeleteEvent event) {
            LogUtils.info("DataEventListener ----- RelationalDeleteEvent onApplicationEvent {}", event);
        }
    }

    @Component
    public static class RelationalEventWithEntityListener implements ApplicationListener<RelationalEventWithEntity> {
        @Override
        public void onApplicationEvent(RelationalEventWithEntity event) {
            LogUtils.info("DataEventListener ----- RelationalEventWithEntity onApplicationEvent {}", event);
        }
    }

    @Component
    public static class MappingContextEventListener implements ApplicationListener<MappingContextEvent> {
        @Override
        public void onApplicationEvent(MappingContextEvent event) {
            LogUtils.info("DataEventListener ----- MappingContextEvent onApplicationEvent {}", event);
        }
    }

    @Component
    public static class BeforeSaveEventListener implements ApplicationListener<BeforeSaveEvent> {
        @Override
        public void onApplicationEvent(BeforeSaveEvent event) {
            LogUtils.info("DataEventListener ----- BeforeSaveEvent onApplicationEvent {}", event);
        }
    }

    @Component
    public static class BeforeConvertEventListener implements ApplicationListener<BeforeConvertEvent> {
        @Override
        public void onApplicationEvent(BeforeConvertEvent event) {
            LogUtils.info("DataEventListener ----- BeforeConvertEvent onApplicationEvent {}", event);
        }
    }

    @Component
    public static class BeforeDeleteEventListener implements ApplicationListener<BeforeDeleteEvent> {
        @Override
        public void onApplicationEvent(BeforeDeleteEvent event) {
            LogUtils.info("DataEventListener ----- BeforeDeleteEvent onApplicationEvent {}", event);
        }
    }

    @Component
    public static class AfterSaveEventListener implements ApplicationListener<AfterSaveEvent> {
        @Override
        public void onApplicationEvent(AfterSaveEvent event) {
            LogUtils.info("DataEventListener ----- AfterSaveEvent onApplicationEvent {}", event);
        }
    }

    @Component
    public static class AfterDeleteEventListener implements ApplicationListener<AfterDeleteEvent> {
        @Override
        public void onApplicationEvent(AfterDeleteEvent event) {
            LogUtils.info("DataEventListener ----- AfterDeleteEvent onApplicationEvent {}", event);
        }
    }
}
