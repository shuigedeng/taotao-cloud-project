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

package com.taotao.cloud.monitor.configuration;

import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.cloud.monitor.api.PrometheusApi;
import com.taotao.cloud.monitor.api.ReactivePrometheusApi;
import com.taotao.cloud.monitor.model.AlertMessage;
import com.taotao.cloud.monitor.properties.PrometheusProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.cloud.client.ConditionalOnReactiveDiscoveryEnabled;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

/**
 * ## 功能
 * - `Spring cloud` 对接 Prometheus `http_sd`，支持 `servlet` 和 `webflux`，建议集成到 Spring boot admin 这类非业务服务中。
 *
 * ### 添加配置
 * ```yaml
 * - job_name: taotao-cloud
 *   honor_timestamps: true
 *   scrape_interval: 15s
 *   scrape_timeout: 10s
 *   metrics_path: /actuator/prometheus
 *   scheme: http
 *   http_sd_configs:
 *   - url: 'http://{ip}:{port}/actuator/prometheus/sd'
 * ```
 *
 * ## alert web hook
 *
 * ### 添加配置
 * ```yaml
 * receivers:
 * - name: "alerts"
 *   webhook_configs:
 *   - url: 'http://{ip}:{port}/actuator/prometheus/alerts'
 *     send_resolved: true
 * ```
 *
 * ### 自定义监听事件并处理
 * ```java
 * @Async
 * @EventListener
 * public void onAlertEvent(AlertMessage message) {
 * 	// 处理 alert webhook message
 * }
 * ```
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:01:42
 */
@Configuration
@EnableConfigurationProperties({PrometheusProperties.class})
@ConditionalOnProperty(prefix = PrometheusProperties.PREFIX, name = "enabled", havingValue = "true")
public class PrometheusConfiguration {

    @Configuration
    @ConditionalOnBean(DiscoveryClient.class)
    @ConditionalOnDiscoveryEnabled
    // @ConditionalOnBlockingDiscoveryEnabled
    // @ConditionalOnProperty(value = "spring.cloud.discovery.blocking.enabled")
    public static class PrometheusApiConfiguration {

        @Bean
        public PrometheusApi prometheusApi(
                DiscoveryClient discoveryClient, ApplicationEventPublisher eventPublisher) {
            return new PrometheusApi(discoveryClient, eventPublisher);
        }

        @Async
        @EventListener
        public void onAlertEvent(AlertMessage message) {
            // 处理 alert webhook message
            LogUtils.info(message.toString());
        }
    }

    @Configuration
    @ConditionalOnBean(ReactiveDiscoveryClient.class)
    @ConditionalOnDiscoveryEnabled
    @ConditionalOnReactiveDiscoveryEnabled
    public static class ReactivePrometheusApiConfiguration {

        @Bean
        public ReactivePrometheusApi reactivePrometheusApi(
                ReactiveDiscoveryClient discoveryClient, ApplicationEventPublisher eventPublisher) {
            return new ReactivePrometheusApi(discoveryClient, eventPublisher);
        }
    }
}
