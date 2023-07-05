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

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.gateway.properties.FilterProperties;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import org.apache.commons.lang3.ObjectUtils;
import org.dromara.hutool.core.text.StrUtil;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultRequest;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.LoadBalancerUriTools;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.cloud.gateway.config.GatewayReactiveLoadBalancerClientAutoConfiguration;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter;
import org.springframework.cloud.gateway.support.DelegatingServiceInstance;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 基于nacos元数据version灰度发布
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/5/2 19:33
 */
@Configuration
@EnableConfigurationProperties(GatewayLoadBalancerProperties.class)
@ConditionalOnProperty(prefix = FilterProperties.PREFIX, name = "gray", havingValue = "true", matchIfMissing = true)
@AutoConfigureBefore(GatewayReactiveLoadBalancerClientAutoConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class GrayConfiguration {

    @Bean
    @ConditionalOnBean
    public ReactiveLoadBalancerClientFilter gatewayLoadBalancerClientFilter(
            LoadBalancerClientFactory clientFactory, GatewayLoadBalancerProperties gatewayLoadBalancerProperties) {
        return new GrayReactiveLoadBalancerClientFilter(clientFactory, gatewayLoadBalancerProperties);
    }

    /**
     * 灰度过滤器
     *
     * @author shuigedeng
     * @version 2022.03
     * @since 2020/5/2 19:33
     */
    public static class GrayReactiveLoadBalancerClientFilter extends ReactiveLoadBalancerClientFilter {

        private static final int LOAD_BALANCER_CLIENT_FILTER_ORDER = 10150;

        private final GatewayLoadBalancerProperties gatewayLoadBalancerProperties;
        private final LoadBalancerClientFactory clientFactory;

        public GrayReactiveLoadBalancerClientFilter(
                LoadBalancerClientFactory clientFactory, GatewayLoadBalancerProperties gatewayLoadBalancerProperties) {
            super(clientFactory, gatewayLoadBalancerProperties);
            this.gatewayLoadBalancerProperties = gatewayLoadBalancerProperties;
            this.clientFactory = clientFactory;
        }

        @Override
        public int getOrder() {
            return LOAD_BALANCER_CLIENT_FILTER_ORDER;
        }

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            URI url = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
            String schemePrefix = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_SCHEME_PREFIX_ATTR);

            // 判断url 前缀 如不是grayLb开头的就进行下一个过滤器
            if (url != null && ("grayLb".equals(url.getScheme()) || "grayLb".equals(schemePrefix))) {
                return chain.filter(exchange);
            }

            // 判断url 前缀 如不是lb开头的就进行下一个过滤器
            if (url == null || (!"lb".equals(url.getScheme()) && !"lb".equals(schemePrefix))) {
                return chain.filter(exchange);
            }

            // 根据网关的原始网址。替换exchange url为 http://IP:PORT/path 路径的url
            ServerWebExchangeUtils.addOriginalRequestUrl(exchange, url);

            LogUtils.info(ReactiveLoadBalancerClientFilter.class.getSimpleName() + " url before: {}", url);

            return choose(exchange)
                    .doOnNext(response -> {
                        if (!response.hasServer()) {
                            throw NotFoundException.create(
                                    gatewayLoadBalancerProperties.isUse404(),
                                    "Unable to find instance for " + url.getHost());
                        }

                        URI uri = exchange.getRequest().getURI();

                        // if the `lb:<scheme>` mechanism was used, use `<scheme>` as the default,
                        // if the loadbalancer doesn't provide one.
                        String overrideScheme = null;
                        if (schemePrefix != null) {
                            overrideScheme = url.getScheme();
                        }

                        DelegatingServiceInstance serviceInstance =
                                new DelegatingServiceInstance(response.getServer(), overrideScheme);

                        URI requestUrl = LoadBalancerUriTools.reconstructURI(serviceInstance, uri);

                        LogUtils.info("LoadBalancerClientFilter url chosen: " + requestUrl);

                        exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, requestUrl);
                    })
                    .then(chain.filter(exchange));
        }

        private Mono<Response<ServiceInstance>> choose(ServerWebExchange exchange) {
            URI uri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);

            if (Objects.nonNull(uri)) {
                GrayLoadBalancer grayLoadBalancer = new GrayLoadBalancer(
                        uri.getHost(), clientFactory.getLazyProvider(uri.getHost(), ServiceInstanceListSupplier.class));
                return grayLoadBalancer.choose(this.createRequest(exchange));
            }
            return Mono.just(new EmptyResponse());
        }

        private Request<HttpHeaders> createRequest(ServerWebExchange exchange) {
            HttpHeaders headers = exchange.getRequest().getHeaders();
            return new DefaultRequest<>(headers);
        }
    }

    /**
     * GrayLoadBalancer<br>
     *
     * @author shuigedeng
     * @version 2022.03
     * @since 2020/4/27 13:59
     */
    public static class GrayLoadBalancer implements ReactorServiceInstanceLoadBalancer {

        private final String serviceId;
        private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

        public GrayLoadBalancer(
                String serviceId, ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider) {
            this.serviceId = serviceId;
            this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        }

        @Override
        public Mono<Response<ServiceInstance>> choose(Request request) {
            HttpHeaders headers = (HttpHeaders) request.getContext();
            if (this.serviceInstanceListSupplierProvider != null) {
                ServiceInstanceListSupplier supplier =
                        this.serviceInstanceListSupplierProvider.getIfAvailable(NoopServiceInstanceListSupplier::new);

                return supplier.get()
                        .next()
                        .map(list -> getInstanceResponse((List<ServiceInstance>) list, headers));
            }
            return null;
        }

        private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances, HttpHeaders headers) {
            if (instances.isEmpty()) {
                return getServiceInstanceEmptyResponse();
            } else {
                return getServiceInstanceResponseWithGray(instances, headers);
            }
        }

        private Response<ServiceInstance> getServiceInstanceResponseWithGray(
                List<ServiceInstance> instances, HttpHeaders headers) {
            String reqVersion = headers.getFirst(CommonConstant.TAOTAO_CLOUD_REQUEST_VERSION_HEADER);

            // 根据版本进行分发
            if (StrUtil.isNotBlank(reqVersion)) {
                Map<String, String> versionMap = new HashMap<>();
                versionMap.put("version", reqVersion);

                final Set<Entry<String, String>> attributes = Collections.unmodifiableSet(versionMap.entrySet());
                ServiceInstance serviceInstance = null;
                for (ServiceInstance instance : instances) {
                    Map<String, String> metadata = instance.getMetadata();
                    if (metadata.entrySet().containsAll(attributes)) {
                        serviceInstance = instance;
                        break;
                    }
                }
                if (ObjectUtils.isEmpty(serviceInstance)) {
                    return getServiceInstanceEmptyResponse();
                }
                return new DefaultResponse(serviceInstance);
            }

            // 根据在nacos中配置的权重值，进行分发
            Map<ServiceInstance, Integer> weightMap = new HashMap<>();
            for (ServiceInstance instance : instances) {
                Map<String, String> metadata = instance.getMetadata();
                LogUtils.info(
                        "taotao cloud gray loadbalancer nacos version: {} , weight: {}",
                        metadata.get("version"),
                        metadata.get("weight"));

                if (metadata.containsKey("weight")) {
                    weightMap.put(instance, Integer.valueOf(metadata.get("weight")));
                }
            }

            WeightMeta<ServiceInstance> weightMeta = WeightRandomUtils.buildWeightMeta(weightMap);
            if (ObjectUtils.isEmpty(weightMeta)) {
                return getServiceInstanceEmptyResponse();
            }

            ServiceInstance serviceInstance = weightMeta.random();
            if (ObjectUtils.isEmpty(serviceInstance)) {
                return getServiceInstanceEmptyResponse();
            }
            return new DefaultResponse(serviceInstance);
        }

        private Response<ServiceInstance> getServiceInstanceEmptyResponse() {
            LogUtils.warn("No servers available for service: " + this.serviceId);
            return new EmptyResponse();
        }
    }

    /**
     * WeightMeta<br>
     *
     * @author shuigedeng
     * @version 2022.03
     * @since 2020/4/29 22:10
     */
    public static class WeightMeta<T> {

        private final Random ran = new Random();
        private final T[] nodes;
        private final int[] weights;
        private final int maxW;

        public WeightMeta(T[] nodes, int[] weights) {
            this.nodes = nodes;
            this.weights = weights;
            this.maxW = weights[weights.length - 1];
        }

        /**
         * 该方法返回权重随机对象
         *
         * @return t
         */
        public T random() {
            int index = Arrays.binarySearch(weights, ran.nextInt(maxW) + 1);
            if (index < 0) {
                index = -1 - index;
            }
            return nodes[index];
        }

        public T random(int ranInt) {
            if (ranInt > maxW) {
                ranInt = maxW;
            } else if (ranInt < 0) {
                ranInt = 1;
            } else {
                ranInt++;
            }
            int index = Arrays.binarySearch(weights, ranInt);
            if (index < 0) {
                index = -1 - index;
            }
            return nodes[index];
        }

        @Override
        public String toString() {
            StringBuilder l1 = new StringBuilder();
            StringBuilder l2 = new StringBuilder("[random]\t");
            StringBuilder l3 = new StringBuilder("[node]\t\t");
            l1.append(this.getClass().getName())
                    .append(":")
                    .append(this.hashCode())
                    .append(":\n")
                    .append("[index]\t\t");
            for (int i = 0; i < weights.length; i++) {
                l1.append(i).append("\t");
                l2.append(weights[i]).append("\t");
                l3.append(nodes[i]).append("\t");
            }
            l1.append("\n");
            l2.append("\n");
            l3.append("\n");
            return l1.append(l2).append(l3).toString();
        }
    }

    /**
     * WeightRandomUtils<br>
     *
     * @author shuigedeng
     * @version 2022.03
     * @since 2020/4/29 22:10
     */
    public static class WeightRandomUtils {

        public static <T> WeightMeta<T> buildWeightMeta(final Map<T, Integer> weightMap) {
            if (weightMap.isEmpty()) {
                return null;
            }
            final int size = weightMap.size();
            Object[] nodes = new Object[size];
            int[] weights = new int[size];
            int index = 0;
            int weightAdder = 0;
            for (Entry<T, Integer> each : weightMap.entrySet()) {
                nodes[index] = each.getKey();
                weights[index++] = (weightAdder = weightAdder + each.getValue());
            }
            return new WeightMeta<>((T[]) nodes, weights);
        }
    }
}
