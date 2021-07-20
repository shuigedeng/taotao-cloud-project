/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.loadbalancer.isolation;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.context.LbIsolationContextHolder;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
import org.springframework.cloud.loadbalancer.core.SelectedInstanceCallback;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

/**
 * CustomLoadBalancer
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/07/20 08:31
 */
public class CustomLoadBalancer implements ReactorServiceInstanceLoadBalancer {

	private static final Log log = LogFactory.getLog(RoundRobinLoadBalancer.class);


	ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

	/**
	 * @param serviceInstanceListSupplierProvider a provider of {@link ServiceInstanceListSupplier}
	 *                                            that will be used to get available instances
	 */
	public CustomLoadBalancer(
		ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider) {
		this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
	}


	@SuppressWarnings("rawtypes")
	@Override
	public Mono<Response<ServiceInstance>> choose(Request request) {
		ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider
			.getIfAvailable(NoopServiceInstanceListSupplier::new);
		return supplier.get(request).next()
			.map(serviceInstances -> processInstanceResponse(supplier, serviceInstances));
	}

	private Response<ServiceInstance> processInstanceResponse(ServiceInstanceListSupplier supplier,
		List<ServiceInstance> serviceInstances) {
		Response<ServiceInstance> serviceInstanceResponse = getInstanceResponse(serviceInstances);
		if (supplier instanceof SelectedInstanceCallback && serviceInstanceResponse.hasServer()) {
			((SelectedInstanceCallback) supplier)
				.selectedServiceInstance(serviceInstanceResponse.getServer());
		}
		return serviceInstanceResponse;
	}

	private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances) {
		if (instances.isEmpty()) {
			if (log.isWarnEnabled()) {
				log.warn("No servers available for service: ");
			}
			return new EmptyResponse();
		}

		List<ServiceInstance> targetList = null;
		String version = LbIsolationContextHolder.getVersion();
		if (StrUtil.isNotEmpty(version)) {
			//取指定版本号的实例
			targetList = instances.stream().filter(
				server -> version.equals(
					server.getMetadata().get(CommonConstant.METADATA_VERSION)
				)
			).collect(Collectors.toList());
		}

		if (CollUtil.isEmpty(targetList)) {
			//只取无版本号的实例
			targetList = instances.stream().filter(
				server -> {
					String metadataVersion = server.getMetadata()
						.get(CommonConstant.METADATA_VERSION);
					return StrUtil.isEmpty(metadataVersion);
				}
			).collect(Collectors.toList());
		}

		if (CollUtil.isNotEmpty(targetList)) {
			return new DefaultResponse(getServer(targetList));
		}

		return new DefaultResponse(instances.get(RandomUtil.randomInt(instances.size())));
	}

	/**
	 * 随机取一个实例
	 */
	private ServiceInstance getServer(List<ServiceInstance> upList) {
		int nextInt = RandomUtil.randomInt(upList.size());
		return upList.get(nextInt);
	}
}
