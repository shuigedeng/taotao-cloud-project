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

package com.taotao.cloud.gateway.configuration;

import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.cloud.gateway.filter.global.loadbalancer.GrayReactiveLoadBalancerClientFilter;
import com.taotao.cloud.gateway.properties.FilterProperties;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.CompletionContext;
import org.springframework.cloud.client.loadbalancer.LoadBalancerLifecycle;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerClientRequestTransformer;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.cloud.gateway.config.GatewayReactiveLoadBalancerClientAutoConfiguration;
import org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;

/**
 * 基于nacos元数据version灰度发布
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/5/2 19:33
 */
@Configuration
@EnableConfigurationProperties(GatewayLoadBalancerProperties.class)
@ConditionalOnProperty(
        prefix = FilterProperties.PREFIX,
        name = "gray",
        havingValue = "true",
        matchIfMissing = true)
@AutoConfigureBefore(GatewayReactiveLoadBalancerClientAutoConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class GrayLoadBalancerConfiguration {

    @Bean
    public ReactiveLoadBalancerClientFilter grayReactiveLoadBalancerClientFilter(
            LoadBalancerClientFactory clientFactory,
            GatewayLoadBalancerProperties gatewayLoadBalancerProperties ) {
        return new GrayReactiveLoadBalancerClientFilter(
                clientFactory, gatewayLoadBalancerProperties);
    }

    /**
     * CustomLoadBalancerLifecycle
     *
     * @author shuigedeng
     * @version 2026.01
     * @since 2025-12-19 09:30:45
     */
    @Component
    public static class CustomLoadBalancerLifecycle implements LoadBalancerLifecycle<Object, Object, ServiceInstance> {

        @Override
        public void onStart( Request request ) {
            LogUtils.info("LoadBalancerLifecycle start=========================");
        }

        @Override
        public void onStartRequest( Request request, Response lbResponse ) {
            LogUtils.info("LoadBalancerLifecycle onStartRequest=========================");
        }

        @Override
        public void onComplete( CompletionContext completionContext ) {
            LogUtils.info("LoadBalancerLifecycle onComplete=========================");
        }
    }

    // 转换负载平衡的HTTP请求
    // For WebClient,
    @Bean
    public LoadBalancerClientRequestTransformer transformer() {
        return new LoadBalancerClientRequestTransformer() {
            @Override
            public ClientRequest transformRequest( ClientRequest request, ServiceInstance instance ) {
                return ClientRequest.from(request)
                        .header("X-InstanceId", instance.getInstanceId())
                        .build();
            }
        };
    }

    // For RestTemplate
    //    @Bean
    //    public LoadBalancerRequestTransformer transformer1() {
    //        return new LoadBalancerRequestTransformer() {
    //            @Override
    //            public HttpRequest transformRequest(HttpRequest request, ServiceInstance instance)
    // {
    //                return new HttpRequestWrapper(request) {
    //                    @Override
    //                    public HttpHeaders getHeaders() {
    //                        HttpHeaders headers = new HttpHeaders();
    //                        headers.putAll(super.getHeaders());
    //                        headers.add("X-InstanceId", instance.getInstanceId());
    //                        return headers;
    //                    }
    //                };
    //            }
    //        };

    //    @Bean
    //    ReactorLoadBalancer<ServiceInstance> randomLoadBalancer(Environment environment,
    //                                                            LoadBalancerClientFactory
    // loadBalancerClientFactory) {
    //        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
    //        return new RandomLoadBalancer(loadBalancerClientFactory
    //                .getLazyProvider(name, ServiceInstanceListSupplier.class),
    //                name);
    //    }

    // 加权负载平衡
    //    @Bean
    //    public ServiceInstanceListSupplier discoveryClientServiceInstanceListSupplier(
    //            ConfigurableApplicationContext context) {
    //        return ServiceInstanceListSupplier.builder()
    //                .withDiscoveryClient()
    //                .withWeighted()
    //              .withWeighted(instance -> ThreadLocalRandom.current().nextInt(1, 101))
    //                .withCaching()
    //                .build(context);
    //    }

    // 基于区域的负载平衡
    //    @Bean
    //    public ServiceInstanceListSupplier discoveryClientServiceInstanceListSupplier(
    //            ConfigurableApplicationContext context) {
    //        return ServiceInstanceListSupplier.builder()
    //                .withDiscoveryClient()
    //                .withCaching()
    //                .withZonePreference()
    //                .build(context);
    //    }

    // LoadBalancer的实例运行状况检查负载平衡
    //    @Bean
    //    public ServiceInstanceListSupplier discoveryClientServiceInstanceListSupplier(
    //            ConfigurableApplicationContext context) {
    //        return ServiceInstanceListSupplier.builder()
    //                .withDiscoveryClient()
    //                .withHealthChecks()
    //                .build(context);
    //    }

    // LoadBalancer的实例首选项相同负载平衡 在这种模式中，可以设置LoadBalancer优先选择先前选择过的服务实例(如果该实例可用的话)。
    //    @Bean
    //    public ServiceInstanceListSupplier discoveryClientServiceInstanceListSupplier(
    //            ConfigurableApplicationContext context) {
    //        return ServiceInstanceListSupplier.builder()
    //                .withDiscoveryClient()
    //                .withSameInstancePreference()
    //                .build(context);
    //    }

    // LoadBalancer的基于请求的粘性会话负载平衡 在这种模式中，LoadBalancer优先选择与请求携带的cookie中提供的instanceId对应的服务实例。
    //    @Bean
    //    public ServiceInstanceListSupplier discoveryClientServiceInstanceListSupplier(
    //            ConfigurableApplicationContext context) {
    //        return ServiceInstanceListSupplier.builder()
    //                .withDiscoveryClient()
    //                .withRequestBasedStickySession()
    //                .build(context);
    //    }

}
