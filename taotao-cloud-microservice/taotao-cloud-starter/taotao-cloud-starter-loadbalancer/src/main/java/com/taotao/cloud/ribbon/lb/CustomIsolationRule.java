// /*
//  * Copyright 2017-2020 original authors
//  *
//  * Licensed under the Apache License, Version 2.0 (the "License");
//  * you may not use this file except in compliance with the License.
//  * You may obtain a copy of the License at
//  *
//  * https://www.apache.org/licenses/LICENSE-2.0
//  *
//  * Unless required by applicable law or agreed to in writing, software
//  * distributed under the License is distributed on an "AS IS" BASIS,
//  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  * See the License for the specific language governing permissions and
//  * limitations under the License.
//  */
// package com.taotao.cloud.ribbon.lb;
//
// import cn.hutool.core.collection.CollUtil;
// import cn.hutool.core.util.RandomUtil;
// import cn.hutool.core.util.StrUtil;
// import com.alibaba.cloud.nacos.ribbon.NacosServer;
// import com.netflix.loadbalancer.ILoadBalancer;
// import com.netflix.loadbalancer.Server;
// import com.taotao.cloud.common.constant.CommonConstant;
// import com.taotao.cloud.common.context.LbIsolationContextHolder;
// import org.springframework.beans.factory.ObjectProvider;
// import org.springframework.cloud.client.ServiceInstance;
// import org.springframework.cloud.client.loadbalancer.Request;
// import org.springframework.cloud.client.loadbalancer.Response;
// import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
// import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
// import reactor.core.publisher.Mono;
//
// import java.util.List;
// import java.util.stream.Collectors;
//
// /**
//  * 自定义隔离随机规则
//  *
//  * @author dengtao
//  * @date 2020/6/15 11:31
//  * @since v1.0
//  */
// public class CustomIsolationRule extends RoundRobinLoadBalancer {
//
// 	private final static String KEY_DEFAULT = "default";
//
// 	public CustomIsolationRule(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId, int seedPosition) {
// 		super(serviceInstanceListSupplierProvider, serviceId, seedPosition);
// 	}
//
// 	/**
// 	 * 优先根据版本号取实例
// 	 */
// 	public Server choose(ILoadBalancer lb, Object key) {
// 		if (lb == null) {
// 			return null;
// 		}
//
// 		String version;
// 		if (key != null && !KEY_DEFAULT.equals(key)) {
// 			version = key.toString();
// 		} else {
// 			version = LbIsolationContextHolder.getVersion();
// 		}
// 		List<Server> targetList = null;
// 		List<Server> upList = lb.getReachableServers();
// 		if (StrUtil.isNotEmpty(version)) {
// 			//取指定版本号的实例
// 			targetList = upList.stream().filter(
// 				server -> version.equals(
// 					((NacosServer) server).getMetadata().get(CommonConstant.METADATA_VERSION)
// 				)
// 			).collect(Collectors.toList());
// 		}
//
// 		if (CollUtil.isEmpty(targetList)) {
// 			//只取无版本号的实例
// 			targetList = upList.stream().filter(
// 				server -> {
// 					String metadataVersion = ((NacosServer) server).getMetadata().get(CommonConstant.METADATA_VERSION);
// 					return StrUtil.isEmpty(metadataVersion);
// 				}
// 			).collect(Collectors.toList());
// 		}
//
// 		if (CollUtil.isNotEmpty(targetList)) {
// 			return getServer(targetList);
// 		}
// 		return super.choose(lb, key);
// 	}
//
// 	/**
// 	 * 随机取一个实例
// 	 */
// 	private Server getServer(List<Server> upList) {
// 		int nextInt = RandomUtil.randomInt(upList.size());
// 		return upList.get(nextInt);
// 	}
//
// 	@Override
// 	public Mono<Response<ServiceInstance>> choose() {
// 		return null;
// 	}
//
// 	@Override
// 	public Mono<Response<ServiceInstance>> choose(Request request){
// 		return null;
// 	}
//
// }
//
