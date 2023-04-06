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
import org.springframework.kafka.event.ConsumerFailedToStartEvent;
import org.springframework.kafka.event.ConsumerPartitionPausedEvent;
import org.springframework.kafka.event.ConsumerPartitionResumedEvent;
import org.springframework.kafka.event.ConsumerPausedEvent;
import org.springframework.kafka.event.ConsumerResumedEvent;
import org.springframework.kafka.event.ConsumerStartedEvent;
import org.springframework.kafka.event.ConsumerStoppedEvent;
import org.springframework.kafka.event.ContainerStoppedEvent;
import org.springframework.kafka.event.ListenerContainerIdleEvent;
import org.springframework.kafka.event.ListenerContainerPartitionIdleEvent;
import org.springframework.kafka.event.NonResponsiveConsumerEvent;
import org.springframework.stereotype.Component;

@Configuration
public class KafkaEventListener {
    @Component
    public static class ConsumerFailedToStartEventListener implements ApplicationListener<ConsumerFailedToStartEvent> {
        @Override
        public void onApplicationEvent(ConsumerFailedToStartEvent event) {
            LogUtils.info("KafkaEventListener ----- ConsumerFailedToStartEvent onApplicationEvent {}", event);
        }
    }

    @Component
    public static class ConsumerPartitionPausedEventEventListener
            implements ApplicationListener<ConsumerPartitionPausedEvent> {
        @Override
        public void onApplicationEvent(ConsumerPartitionPausedEvent event) {
            LogUtils.info("KafkaEventListener ----- ConsumerPartitionPausedEvent onApplicationEvent {}", event);
        }
    }

    @Component
    public static class ConsumerPartitionResumedEventListener
            implements ApplicationListener<ConsumerPartitionResumedEvent> {
        @Override
        public void onApplicationEvent(ConsumerPartitionResumedEvent event) {
            LogUtils.info("KafkaEventListener ----- ConsumerPartitionResumedEvent onApplicationEvent {}", event);
        }
    }

    @Component
    public static class ConsumerPausedEventListener implements ApplicationListener<ConsumerPausedEvent> {
        @Override
        public void onApplicationEvent(ConsumerPausedEvent event) {
            LogUtils.info("KafkaEventListener ----- ConsumerPausedEvent onApplicationEvent {}", event);
        }
    }

    @Component
    public static class ConsumerResumedEventListener implements ApplicationListener<ConsumerResumedEvent> {
        @Override
        public void onApplicationEvent(ConsumerResumedEvent event) {
            LogUtils.info("KafkaEventListener ----- ConsumerResumedEvent onApplicationEvent {}", event);
        }
    }

    @Component
    public static class ConsumerStartedEventListener implements ApplicationListener<ConsumerStartedEvent> {
        @Override
        public void onApplicationEvent(ConsumerStartedEvent event) {
            LogUtils.info("KafkaEventListener ----- ConsumerStartedEvent onApplicationEvent {}", event);
        }
    }

    @Component
    public static class ConsumerStoppedEventListener implements ApplicationListener<ConsumerStoppedEvent> {
        @Override
        public void onApplicationEvent(ConsumerStoppedEvent event) {
            LogUtils.info("KafkaEventListener ----- ConsumerStoppedEvent onApplicationEvent {}", event);
        }
    }

    @Component
    public static class ContainerStoppedEventListener implements ApplicationListener<ContainerStoppedEvent> {
        @Override
        public void onApplicationEvent(ContainerStoppedEvent event) {
            LogUtils.info("KafkaEventListener ----- ContainerStoppedEvent onApplicationEvent {}", event);
        }
    }

    @Component
    public static class ListenerContainerIdleEventListener implements ApplicationListener<ListenerContainerIdleEvent> {
        @Override
        public void onApplicationEvent(ListenerContainerIdleEvent event) {
            LogUtils.info("KafkaEventListener ----- ListenerContainerIdleEvent onApplicationEvent {}", event);
        }
    }

    @Component
    public static class ListenerContainerPartitionIdleEventListener
            implements ApplicationListener<ListenerContainerPartitionIdleEvent> {
        @Override
        public void onApplicationEvent(ListenerContainerPartitionIdleEvent event) {
            LogUtils.info(
                    "KafkaEventListener ----- ListenerContainerPartitionIdleEvent" + " onApplicationEvent {}", event);
        }
    }

    @Component
    public static class NonResponsiveConsumerEventListener implements ApplicationListener<NonResponsiveConsumerEvent> {
        @Override
        public void onApplicationEvent(NonResponsiveConsumerEvent event) {
            LogUtils.info("KafkaEventListener ----- NonResponsiveConsumerEvent onApplicationEvent {}", event);
        }
    }
}
