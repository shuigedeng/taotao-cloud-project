///*
// * Copyright 2002-2021 the original author or authors.
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
//package com.taotao.cloud.gateway.route;
//
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.util.StrUtil;
//import com.alibaba.cloud.nacos.NacosConfigProperties;
//import com.alibaba.fastjson.JSONObject;
//import com.alibaba.nacos.api.NacosFactory;
//import com.alibaba.nacos.api.config.listener.Listener;
//import com.alibaba.nacos.api.exception.NacosException;
//import com.taotao.cloud.common.utils.LogUtil;
//import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
//import org.springframework.cloud.gateway.route.RouteDefinition;
//import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
//import org.springframework.context.ApplicationEventPublisher;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.Executor;
//
///**
// * nacos路由数据源
// *
// * @author dengtao
// * @since 2020/5/2 19:26
// * @version 1.0.0
// */
//public class NacosRouteDefinitionRepository implements RouteDefinitionRepository {
//	private static final String TAOTAO_CLOUD_DATA_ID = "taotao-cloud-routes";
//	private static final String TAOTAO_CLOUD_GROUP_ID = "TAOTAO_CLOUD_GATEWAY";
//
//	private final ApplicationEventPublisher publisher;
//
//	private final NacosConfigProperties nacosConfigProperties;
//
//	public NacosRouteDefinitionRepository(ApplicationEventPublisher publisher,
//										  NacosConfigProperties nacosConfigProperties) {
//		this.publisher = publisher;
//		this.nacosConfigProperties = nacosConfigProperties;
//		addListener();
//	}
//
//	@Override
//	public Flux<RouteDefinition> getRouteDefinitions() {
//		try {
//			String content = NacosFactory.createConfigService(nacosConfigProperties.assembleConfigServiceProperties())
//				.getConfig(TAOTAO_CLOUD_DATA_ID, TAOTAO_CLOUD_GROUP_ID, 5000);
//			List<RouteDefinition> routeDefinitions = getListByStr(content);
//			return Flux.fromIterable(routeDefinitions);
//		} catch (NacosException e) {
//			LogUtil.error("getRouteDefinitions by nacos error", e);
//		}
//		return Flux.fromIterable(CollUtil.newArrayList());
//	}
//
//
//	private void addListener() {
//		try {
//			NacosFactory.createConfigService(nacosConfigProperties.assembleConfigServiceProperties())
//				.addListener(TAOTAO_CLOUD_DATA_ID, TAOTAO_CLOUD_GROUP_ID, new Listener() {
//					@Override
//					public Executor getExecutor() {
//						return null;
//					}
//
//					@Override
//					public void receiveConfigInfo(String configInfo) {
//						publisher.publishEvent(new RefreshRoutesEvent(this));
//					}
//				});
//		} catch (NacosException e) {
//			LogUtil.error("nacos-addListener-error", e);
//		}
//	}
//
//	@Override
//	public Mono<Void> save(Mono<RouteDefinition> route) {
//		return null;
//	}
//
//	@Override
//	public Mono<Void> delete(Mono<String> routeId) {
//		return null;
//	}
//
//	private List<RouteDefinition> getListByStr(String content) {
//		if (StrUtil.isNotEmpty(content)) {
//			return JSONObject.parseArray(content, RouteDefinition.class);
//		}
//		return new ArrayList<>(0);
//	}
//}
