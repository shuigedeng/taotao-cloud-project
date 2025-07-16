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

import static org.springframework.cloud.loadbalancer.core.CachingServiceInstanceListSupplier.SERVICE_INSTANCE_CACHE_NAME;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
import com.alibaba.nacos.common.notify.NotifyCenter;
import com.alibaba.nacos.common.notify.listener.Subscriber;
import com.alibaba.nacos.common.utils.JacksonUtils;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.cloud.gateway.properties.ApiProperties;
import java.util.List;
import java.util.Map;
import lombok.*;
import org.dromara.hutool.http.HttpUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 全局配置
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/4/29 22:13
 */
@Configuration
public class NacosInstancesChangeEventListenerConfiguration {

    /**
     * 一个集群中有某个服务突然下线，但是网关还是会去请求这个实例，所以线上就报错
     * <p>
     * Gateway中有个缓存 CachingRouteLocator ，而网关服务使用的是lb模式，服务在上线或者下线之后，未能及时刷新这个缓存 CachingRouteLocator
     * <p>
     * 监听 Nacos 实例刷新事件，一旦出现实例发生变化马上删除缓存。在删除负载均衡缓存后， Spring Cloud Gateway
     * 在处理请求时发现没有缓存会重新拉取一遍服务列表，这样之后都是用的是最新的服务列表了，也就达到了动态感知上下线的目的
     */
    @Component
    @AllArgsConstructor
    public static class CacheEvictNacosInstancesChangeEventListener
            extends Subscriber<InstancesChangeEvent> implements InitializingBean {

        private final CacheManager defaultLoadBalancerCacheManager;

        @Override
        public void onEvent(InstancesChangeEvent event) {
            LogUtils.info("Spring Gateway 接收实例刷新事件：{}, 开始刷新缓存", JacksonUtils.toJson(event));
            Cache cache = defaultLoadBalancerCacheManager.getCache(SERVICE_INSTANCE_CACHE_NAME);
            if (cache != null) {
                cache.evict(event.getServiceName());
            }
            LogUtils.info("Spring Gateway 实例刷新完成");
        }

        @Override
        public Class<? extends com.alibaba.nacos.common.notify.Event> subscribeType() {
            return InstancesChangeEvent.class;
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            NotifyCenter.registerSubscriber(this);
        }
    }

    /**
     * 在实例启动时候请求接口
     */
    @Component
    @AllArgsConstructor
    public static class InstanceInitRequestInstancesChangeEventListener
            extends Subscriber<InstancesChangeEvent> implements InitializingBean {

        private final RouteDefinitionLocator locator;
        private final ApiProperties apiProperties;

        @Override
        public void onEvent(InstancesChangeEvent event) {
            test(event);
        }

        @Override
        public Class<? extends com.alibaba.nacos.common.notify.Event> subscribeType() {
            return InstancesChangeEvent.class;
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            NotifyCenter.registerSubscriber(this);
        }

        private void test(InstancesChangeEvent instancesChangeEvent) {
            List<Instance> instances = instancesChangeEvent.getHosts();
            if (instances.isEmpty()) {
                return;
            }

            String serviceName = instancesChangeEvent.getServiceName();
            if (serviceName.contains("gateway")) {
                return;
            }

            List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
            RouteDefinition routeDefinition =
                    definitions.stream()
                            .filter(e -> e.getId().equals(serviceName))
                            .findFirst()
                            .orElseGet(() -> null);

            String baseUri = apiProperties.getBaseUri();
            if (routeDefinition != null) {
                Map<String, Object> metadata = routeDefinition.getMetadata();
                String requestUriPrefix = (String) metadata.get("request_uri_prefix");

                String uri =
                        "http://192.168.218.2:33333"
                                + baseUri
                                + "/"
                                + requestUriPrefix
                                + "/"
                                + "request/gateway/test";
                String uri1 =
                        "http://192.168.218.2:33333"
                                + baseUri
                                + "/"
                                + requestUriPrefix
                                + "/"
                                + "v3/api-docs";

                new Thread(
                                () -> {
                                    try {
                                        Thread.sleep(10 * 1000);
                                    } catch (InterruptedException e) {
                                        LogUtils.error(e);
                                        return;
                                    }

                                    try {
                                        String s = HttpUtil.get(uri);
                                        String s1 = HttpUtil.get(uri1);
                                    } catch (Exception e) {
                                        LogUtils.info("----------------------------");
                                    }
                                })
                        .start();
            }
        }

        //        @Retryable(retryFor = Exception.class, maxAttempts = 4, backoff = @Backoff(delay =
        // 5000, multiplier = 1.5))
        //        private void requestGatewayTest() {
        //            LogUtils.info("网关请求测试开始--------------------------------");
        //
        //            try {
        //
        //            } catch (Exception ignored) {
        //                throw ignored;
        //            }
        //        }
    }
}
