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
package com.taotao.cloud.gateway.loadBalancer;

import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.gateway.utils.WeightMeta;
import com.taotao.cloud.gateway.utils.WeightRandomUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * GrayLoadBalancer<br>
 *
 * @author dengtao
 * @date 2020/4/27 13:59
 * @since v1.0
 */
public class GrayLoadBalancer implements ReactorServiceInstanceLoadBalancer {
	private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;
	private final String serviceId;

	public GrayLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId) {
		this.serviceId = serviceId;
		this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
	}

	@Override
	public Mono<Response<ServiceInstance>> choose(Request request) {
		HttpHeaders headers = (HttpHeaders) request.getContext();
		if (this.serviceInstanceListSupplierProvider != null) {
			ServiceInstanceListSupplier supplier = this.serviceInstanceListSupplierProvider.getIfAvailable(NoopServiceInstanceListSupplier::new);
			return ((Flux) supplier.get()).next().map(list -> getInstanceResponse((List<ServiceInstance>) list, null));
		}
		return null;
	}


	private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances, HttpHeaders headers) {
		if (instances.isEmpty()) {
			return getServiceInstanceEmptyResponse();
		} else {
			return getServiceInstanceResponseWithWeight(instances);
		}
	}

	/**
	 * 根据版本进行分发
	 */
	private Response<ServiceInstance> getServiceInstanceResponseByVersion(List<ServiceInstance> instances, HttpHeaders headers) {
		String versionNo = headers.getFirst("version");
		Map<String, String> versionMap = new HashMap<>();
		versionMap.put("version", "1");
		final Set<Map.Entry<String, String>> attributes = Collections.unmodifiableSet(versionMap.entrySet());
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

	/**
	 * 根据在nacos中配置的权重值，进行分发
	 */
	private Response<ServiceInstance> getServiceInstanceResponseWithWeight(List<ServiceInstance> instances) {
		Map<ServiceInstance, Integer> weightMap = new HashMap<>();
		for (ServiceInstance instance : instances) {
			Map<String, String> metadata = instance.getMetadata();
			LogUtil.info(metadata.get("version") + "-->weight:" + metadata.get("weight"));
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
		LogUtil.warn("No servers available for service: " + this.serviceId);
		return new EmptyResponse();
	}
}
