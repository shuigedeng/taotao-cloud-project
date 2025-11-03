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

package com.taotao.cloud.gateway.filter.global.loadbalancer;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.*;

import cn.hutool.core.util.StrUtil;
import com.taotao.boot.common.constant.CommonConstants;
import com.taotao.boot.common.utils.log.LogUtils;
import java.net.URI;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.lang3.ObjectUtils;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter;
import org.springframework.cloud.gateway.support.DelegatingServiceInstance;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 从 org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter 复制 重新修改的添加了灰度
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class GrayReactiveLoadBalancerClientFilter extends ReactiveLoadBalancerClientFilter {

    private final LoadBalancerClientFactory clientFactory;
    private final GatewayLoadBalancerProperties properties;

    public GrayReactiveLoadBalancerClientFilter(
            LoadBalancerClientFactory clientFactory, GatewayLoadBalancerProperties properties) {
        super(clientFactory, properties);
        this.clientFactory = clientFactory;
        this.properties = properties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        URI url = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        String schemePrefix = exchange.getAttribute(GATEWAY_SCHEME_PREFIX_ATTR);

        // 是否需要灰度 判断url 前缀 如不是grayLb开头的就进行下一个过滤器
        // boolean graylb = "grayLb".equals(url.getScheme()) || "grayLb".equals(schemePrefix);

        if (url == null || (!"lb".equals(url.getScheme()) && !"lb".equals(schemePrefix))) {
            return chain.filter(exchange);
        }

        // preserve the original url
        addOriginalRequestUrl(exchange, url);

        LogUtils.info(
                GrayReactiveLoadBalancerClientFilter.class.getSimpleName() + " url before: {}",
                url);

        URI requestUri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        String serviceId = requestUri.getHost();
        Set<LoadBalancerLifecycle> supportedLifecycleProcessors =
                LoadBalancerLifecycleValidator.getSupportedLifecycleProcessors(
                        clientFactory.getInstances(serviceId, LoadBalancerLifecycle.class),
                        RequestDataContext.class,
                        ResponseData.class,
                        ServiceInstance.class);

        DefaultRequest<RequestDataContext> lbRequest =
                new DefaultRequest<>(
                        new RequestDataContext(
                                new RequestData(exchange.getRequest()), getHint(serviceId)));

        return choose(lbRequest, serviceId, supportedLifecycleProcessors, url, true)
                .doOnNext(
                        response -> {
                            if (!response.hasServer()) {
                                supportedLifecycleProcessors.forEach(
                                        lifecycle ->
                                                lifecycle.onComplete(
                                                        new CompletionContext<>(
                                                                CompletionContext.Status.DISCARD,
                                                                lbRequest,
                                                                response)));
                                throw NotFoundException.create(
                                        properties.isUse404(),
                                        "Unable to find instance for " + url.getHost());
                            }

                            ServiceInstance retrievedInstance = response.getServer();

                            URI uri = exchange.getRequest().getURI();

                            // if the `lb:<scheme>` mechanism was used, use `<scheme>` as the
                            // default,
                            // if the loadbalancer doesn't provide one.
                            String overrideScheme = retrievedInstance.isSecure() ? "https" : "http";
                            if (schemePrefix != null) {
                                overrideScheme = url.getScheme();
                            }

                            DelegatingServiceInstance serviceInstance =
                                    new DelegatingServiceInstance(
                                            retrievedInstance, overrideScheme);

                            URI requestUrl = reconstructURI(serviceInstance, uri);

                            LogUtils.info("LoadBalancerClientFilter url chosen: " + requestUrl);

                            exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, requestUrl);
                            exchange.getAttributes()
                                    .put(GATEWAY_LOADBALANCER_RESPONSE_ATTR, response);
                            supportedLifecycleProcessors.forEach(
                                    lifecycle -> lifecycle.onStartRequest(lbRequest, response));
                        })
                .then(chain.filter(exchange))
                .doOnError(
                        throwable ->
                                supportedLifecycleProcessors.forEach(
                                        lifecycle ->
                                                lifecycle.onComplete(
                                                        new CompletionContext<
                                                                ResponseData,
                                                                ServiceInstance,
                                                                RequestDataContext>(
                                                                CompletionContext.Status.FAILED,
                                                                throwable,
                                                                lbRequest,
                                                                exchange.getAttribute(
                                                                        GATEWAY_LOADBALANCER_RESPONSE_ATTR)))))
                .doOnSuccess(
                        aVoid ->
                                supportedLifecycleProcessors.forEach(
                                        lifecycle ->
                                                lifecycle.onComplete(
                                                        new CompletionContext<
                                                                ResponseData,
                                                                ServiceInstance,
                                                                RequestDataContext>(
                                                                CompletionContext.Status.SUCCESS,
                                                                lbRequest,
                                                                exchange.getAttribute(
                                                                        GATEWAY_LOADBALANCER_RESPONSE_ATTR),
                                                                new ResponseData(
                                                                        exchange.getResponse(),
                                                                        new RequestData(
                                                                                exchange
                                                                                        .getRequest()))))));
    }

    protected URI reconstructURI(ServiceInstance serviceInstance, URI original) {
        return LoadBalancerUriTools.reconstructURI(serviceInstance, original);
    }

    private Mono<Response<ServiceInstance>> choose(
            Request<RequestDataContext> lbRequest,
            String serviceId,
            Set<LoadBalancerLifecycle> supportedLifecycleProcessors,
            URI uri,
            boolean graylb) {
        if (graylb) {
            GrayLoadBalancer grayLoadBalancer =
                    new GrayLoadBalancer(
                            uri.getHost(),
                            clientFactory.getLazyProvider(
                                    uri.getHost(), ServiceInstanceListSupplier.class));
            return grayLoadBalancer.choose(lbRequest);
        } else {
            // super的代码
            ReactorLoadBalancer<ServiceInstance> loadBalancer =
                    this.clientFactory.getInstance(
                            serviceId, ReactorServiceInstanceLoadBalancer.class);
            if (loadBalancer == null) {
                throw new NotFoundException("No loadbalancer available for " + serviceId);
            }
            supportedLifecycleProcessors.forEach(lifecycle -> lifecycle.onStart(lbRequest));
            return loadBalancer.choose(lbRequest);
        }
    }

    private String getHint(String serviceId) {
        LoadBalancerProperties loadBalancerProperties = clientFactory.getProperties(serviceId);
        Map<String, String> hints = loadBalancerProperties.getHint();
        String defaultHint = hints.getOrDefault("default", "default");
        String hintPropertyValue = hints.get(serviceId);
        return hintPropertyValue != null ? hintPropertyValue : defaultHint;
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
        private final ObjectProvider<ServiceInstanceListSupplier>
                serviceInstanceListSupplierProvider;

        public GrayLoadBalancer(
                String serviceId,
                ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider) {
            this.serviceId = serviceId;
            this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        }

        @Override
        public Mono<Response<ServiceInstance>> choose(Request request) {
            RequestDataContext requestDataContext = (RequestDataContext) request.getContext();
            HttpHeaders headers = requestDataContext.getClientRequest().getHeaders();

            if (this.serviceInstanceListSupplierProvider != null) {
                ServiceInstanceListSupplier supplier =
                        this.serviceInstanceListSupplierProvider.getIfAvailable(
                                NoopServiceInstanceListSupplier::new);

                return supplier.get().next().map(list -> getInstanceResponse(list, headers));
            }
            return null;
        }

        private Response<ServiceInstance> getInstanceResponse(
                List<ServiceInstance> instances, HttpHeaders headers) {
            if (instances.isEmpty()) {
                return getServiceInstanceEmptyResponse();
            } else {
                return getServiceInstanceResponseWithGray(instances, headers);
            }
        }

        private Response<ServiceInstance> getServiceInstanceResponseWithGray(
                List<ServiceInstance> instances, HttpHeaders headers) {
            String reqVersion = headers.getFirst(CommonConstants.TTC_REQUEST_VERSION);
            if (instances.isEmpty()) {
                return getServiceInstanceEmptyResponse();
            }

            // 根据版本进行分发 todo 需要修改
            //            if (StrUtil.isNotBlank(reqVersion)) {
            //                Map<String, String> versionMap = new HashMap<>();
            //                versionMap.put("version", reqVersion);
            //
            //                final Set<Map.Entry<String, String>> attributes =
            // Collections.unmodifiableSet(versionMap.entrySet());
            //                for (ServiceInstance instance : instances) {
            //                    Map<String, String> metadata = instance.getMetadata();
            //                    if (!metadata.entrySet().containsAll(attributes)) {
            //                        instances.remove(instance);
            //                    }
            //                }
            //            }

            String weight = headers.getFirst("taotao-cloud-weight");
            if (StrUtil.isNotBlank(weight)) {
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

                WeightMeta<ServiceInstance> weightMeta =
                        WeightRandomUtils.buildWeightMeta(weightMap);
                if (ObjectUtils.isEmpty(weightMeta)) {
                    return getServiceInstanceEmptyResponse();
                }

                ServiceInstance serviceInstance = weightMeta.random();
                if (ObjectUtils.isEmpty(serviceInstance)) {
                    return getServiceInstanceEmptyResponse();
                }
                return new DefaultResponse(serviceInstance);
            }

            // 最终返回随机
            return getInstanceResponse(instances);
        }

        private Response<ServiceInstance> getServiceInstanceEmptyResponse() {
            LogUtils.warn("No servers available for service: " + this.serviceId);
            return new EmptyResponse();
        }

        private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances) {
            if (instances.isEmpty()) {
                LogUtils.warn("No servers available for service: " + serviceId);
                return new EmptyResponse();
            }
            int index = ThreadLocalRandom.current().nextInt(instances.size());

            ServiceInstance instance = instances.get(index);

            return new DefaultResponse(instance);
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
            for (Map.Entry<T, Integer> each : weightMap.entrySet()) {
                nodes[index] = each.getKey();
                weights[index++] = (weightAdder = weightAdder + each.getValue());
            }

            @SuppressWarnings("unchecked")
            T[] data = (T[]) nodes;
            return new WeightMeta<>(data, weights);
        }
    }
}
