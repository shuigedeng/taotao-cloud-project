///*
// * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.taotao.cloud.gateway.configuration;
//
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.collection.ListUtil;
//import cn.hutool.core.util.StrUtil;
//import com.alibaba.cloud.nacos.NacosConfigProperties;
//import com.alibaba.fastjson2.JSONArray;
//import com.alibaba.fastjson2.JSONObject;
//import com.alibaba.nacos.api.NacosFactory;
//import com.alibaba.nacos.api.config.listener.Listener;
//import com.alibaba.nacos.api.exception.NacosException;
//import com.taotao.boot.common.constant.CommonConstants;
//import com.taotao.boot.common.utils.common.PropertyUtils;
//import com.taotao.boot.common.utils.log.LogUtils;
//import com.taotao.cloud.gateway.properties.DynamicRouteProperties;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.Executor;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.context.ApplicationEventPublisherAware;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
///**
// * 基于nacos动态路由配置
// *
// * @author shuigedeng
// * @version 2022.03
// * @since 2020/5/2 19:33
// */
//@Configuration
//@ConditionalOnProperty(
//        prefix = DynamicRouteProperties.PREFIX,
//        name = "enabled",
//        havingValue = "true")
//public class DynamicRouteConfiguration {
//
//    /**
//     * NacosDynamicRoute
//     *
//     * @author shuigedeng
//     * @version 2026.03
//     * @since 2025-12-19 09:30:45
//     */
//    @Configuration
//    @ConditionalOnProperty(
//            prefix = DynamicRouteProperties.PREFIX,
//            name = "type",
//            havingValue = "nacos",
//            matchIfMissing = false)
//    public static class NacosDynamicRoute {
//
//        @Bean
//        @ConditionalOnBean
//        public NacosRouteDefinitionRepository nacosRouteDefinitionRepository(
//                ApplicationEventPublisher publisher,
//                NacosConfigProperties nacosConfigProperties,
//                DynamicRouteProperties dynamicRouteProperties ) {
//            return new NacosRouteDefinitionRepository(
//                    publisher, nacosConfigProperties, dynamicRouteProperties);
//        }
//    }
//
//    /**
//     * nacos路由数据源
//     *
//     * @author shuigedeng
//     * @version 2022.03
//     * @since 2020/5/2 19:26
//     */
//    public static class NacosRouteDefinitionRepository implements RouteDefinitionRepository {
//
//        private final ApplicationEventPublisher publisher;
//        private final NacosConfigProperties nacosConfigProperties;
//        private final DynamicRouteProperties dynamicRouteProperties;
//
//        public NacosRouteDefinitionRepository(
//                ApplicationEventPublisher publisher,
//                NacosConfigProperties nacosConfigProperties,
//                DynamicRouteProperties dynamicRouteProperties ) {
//            this.publisher = publisher;
//            this.dynamicRouteProperties = dynamicRouteProperties;
//            this.nacosConfigProperties = nacosConfigProperties;
//
//            addListener();
//        }
//
//        @Override
//        public Flux<RouteDefinition> getRouteDefinitions() {
//            try {
//                String content =
//                        NacosFactory.createConfigService(
//                                        nacosConfigProperties.assembleConfigServiceProperties())
//                                .getConfig(
//                                        dynamicRouteProperties.getDataId(),
//                                        dynamicRouteProperties.getGroupId(),
//                                        5000);
//                List<RouteDefinition> routeDefinitions = getListByStr(content);
//                return Flux.fromIterable(routeDefinitions);
//            } catch (NacosException e) {
//                LogUtils.error(
//                        e,
//                        PropertyUtils.getProperty(CommonConstants.SPRING_APP_NAME_KEY)
//                                + "get route definitions from nacos error info: {}",
//                        e.getErrMsg());
//            }
//            return Flux.fromIterable(ListUtil.empty());
//        }
//
//        private void addListener() {
//            try {
//                NacosFactory.createConfigService(
//                                nacosConfigProperties.assembleConfigServiceProperties())
//                        .addListener(
//                                dynamicRouteProperties.getDataId(),
//                                dynamicRouteProperties.getGroupId(),
//                                new Listener() {
//                                    @Override
//                                    public Executor getExecutor() {
//                                        return null;
//                                    }
//
//                                    @Override
//                                    public void receiveConfigInfo( String configInfo ) {
//                                        publisher.publishEvent(new RefreshRoutesEvent(this));
//                                    }
//                                });
//            } catch (NacosException e) {
//                LogUtils.error("nacos addListener error", e);
//            }
//        }
//
//        @Override
//        public Mono<Void> save( Mono<RouteDefinition> route ) {
//            return null;
//        }
//
//        @Override
//        public Mono<Void> delete( Mono<String> routeId ) {
//            return null;
//        }
//
//        private List<RouteDefinition> getListByStr( String content ) {
//            if (StrUtil.isNotEmpty(content)) {
//                return JSONArray.parseArray(content, RouteDefinition.class);
//            }
//            return new ArrayList<>(0);
//        }
//    }
//
//    /**
//     * 动态更新路由网关service 1）实现一个Spring提供的事件推送接口ApplicationEventPublisherAware
//     * 2）提供动态路由的基础方法，可通过获取bean操作该类的方法。该类提供新增路由、更新路由、删除路由，然后实现发布的功能。
//     */
//    @Component
//    public static class DynamicRouteComponent implements ApplicationEventPublisherAware {
//
//        private final RouteDefinitionWriter routeDefinitionWriter;
//
//        private final RouteDefinitionLocator routeDefinitionLocator;
//
//        private ApplicationEventPublisher publisher;
//
//        public DynamicRouteComponent(
//                RouteDefinitionWriter routeDefinitionWriter,
//                RouteDefinitionLocator routeDefinitionLocator ) {
//            this.routeDefinitionWriter = routeDefinitionWriter;
//            this.routeDefinitionLocator = routeDefinitionLocator;
//        }
//
//        @Override
//        public void setApplicationEventPublisher(
//                ApplicationEventPublisher applicationEventPublisher ) {
//            this.publisher = applicationEventPublisher;
//        }
//
//        /**
//         * 删除路由
//         */
//        public String delete( String id ) {
//            try {
//                LogUtils.info("gateway delete route id {}", id);
//                this.routeDefinitionWriter.delete(Mono.just(id)).subscribe();
//                this.publisher.publishEvent(new RefreshRoutesEvent(this));
//                return "delete success";
//            } catch (Exception e) {
//                return "delete fail";
//            }
//        }
//
//        /**
//         * 更新路由
//         */
//        public String updateList( List<RouteDefinition> definitions ) {
//            LogUtils.info("gateway update route {}", definitions);
//            // 删除缓存routerDefinition
//            List<RouteDefinition> routeDefinitionsExits =
//                    routeDefinitionLocator.getRouteDefinitions().buffer().blockFirst();
//            if (!CollUtil.isEmpty(routeDefinitionsExits)) {
//                routeDefinitionsExits.forEach(
//                        routeDefinition -> {
//                            LogUtils.info("delete routeDefinition:{}", routeDefinition);
//                            delete(routeDefinition.getId());
//                        });
//            }
//            definitions.forEach(this::updateById);
//            return "success";
//        }
//
//        /**
//         * 更新路由
//         */
//        public String updateById( RouteDefinition definition ) {
//            try {
//                LogUtils.info("gateway update route {}", definition);
//                this.routeDefinitionWriter.delete(Mono.just(definition.getId()));
//            } catch (Exception e) {
//                return "update fail,not find route  routeId: " + definition.getId();
//            }
//
//            try {
//                routeDefinitionWriter.save(Mono.just(definition)).subscribe();
//                this.publisher.publishEvent(new RefreshRoutesEvent(this));
//                return "success";
//            } catch (Exception e) {
//                return "update route fail";
//            }
//        }
//
//        /**
//         * 增加路由
//         */
//        public String add( RouteDefinition definition ) {
//            LogUtils.info("gateway add route {}", definition);
//            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
//            this.publisher.publishEvent(new RefreshRoutesEvent(this));
//            return "success";
//        }
//    }
//}
