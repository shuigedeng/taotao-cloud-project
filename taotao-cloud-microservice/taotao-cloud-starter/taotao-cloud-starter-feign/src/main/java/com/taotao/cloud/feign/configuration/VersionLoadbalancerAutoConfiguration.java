 /*
  * Copyright 2017-2020 original authors
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  * https://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
 package com.taotao.cloud.feign.configuration;

 import cn.hutool.core.util.StrUtil;
 import com.alibaba.cloud.nacos.NacosServiceInstance;
 import com.taotao.cloud.common.constant.CommonConstant;
 import com.taotao.cloud.common.context.VersionContextHolder;
 import com.taotao.cloud.feign.properties.LbIsolationProperties;
 import java.util.List;
 import java.util.Random;
 import java.util.concurrent.atomic.AtomicInteger;
 import java.util.stream.Collectors;
 import org.apache.commons.logging.Log;
 import org.apache.commons.logging.LogFactory;
 import org.springframework.beans.factory.ObjectProvider;
 import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
 import org.springframework.cloud.client.ServiceInstance;
 import org.springframework.cloud.client.loadbalancer.DefaultRequestContext;
 import org.springframework.cloud.client.loadbalancer.DefaultResponse;
 import org.springframework.cloud.client.loadbalancer.EmptyResponse;
 import org.springframework.cloud.client.loadbalancer.Request;
 import org.springframework.cloud.client.loadbalancer.RequestData;
 import org.springframework.cloud.client.loadbalancer.Response;
 import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
 import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
 import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
 import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
 import org.springframework.cloud.loadbalancer.core.SelectedInstanceCallback;
 import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
 import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
 import org.springframework.context.annotation.Bean;
 import org.springframework.core.env.Environment;
 import org.springframework.http.HttpHeaders;
 import reactor.core.publisher.Mono;

 /**
  * Loadbalancer扩展配置类
  *
  * @author shuigedeng
  * @version 1.0.0
  * @since 2020/6/15 11:31
  */
 public class VersionLoadbalancerAutoConfiguration {

	 @Bean
	 @ConditionalOnProperty(prefix = LbIsolationProperties.PREFIX, name = "enabled", havingValue = "true")
	 public ReactorLoadBalancer<ServiceInstance> reactorServiceInstanceLoadBalancer(
		 Environment environment,
		 LoadBalancerClientFactory loadBalancerClientFactory) {
		 String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
		 return new VersionLoadBalancer(
			 loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class),
			 name);
	 }

	 /**
	  * CustomLoadBalancer
	  *
	  * @author shuigedeng
	  * @version 1.0.0
	  * @since 2021/07/20 08:31
	  */
	 public static class VersionLoadBalancer implements ReactorServiceInstanceLoadBalancer {

		 private static final Log log = LogFactory.getLog(RoundRobinLoadBalancer.class);


		 ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;
		 private final String serviceId;

		 private final AtomicInteger position;

		 /**
		  * @param serviceInstanceListSupplierProvider a provider of {@link ServiceInstanceListSupplier}
		  *                                            that will be used to get available instances
		  */
		 public VersionLoadBalancer(
			 ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider,
			 String serviceId) {
			 this(serviceInstanceListSupplierProvider, serviceId, new Random().nextInt(1000));
		 }

		 public VersionLoadBalancer(
			 ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider,
			 String serviceId, int seedPosition) {
			 this.serviceId = serviceId;
			 this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
			 this.position = new AtomicInteger(seedPosition);
		 }

		 @Override
		 public Mono<Response<ServiceInstance>> choose(Request request) {
			 ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider
				 .getIfAvailable(NoopServiceInstanceListSupplier::new);
			 return supplier
				 .get(request)
				 .next()
				 .map(serviceInstances -> processInstanceResponse(supplier, serviceInstances,
					 request));
		 }

		 private Response<ServiceInstance> processInstanceResponse(
			 ServiceInstanceListSupplier supplier,
			 List<ServiceInstance> serviceInstances, Request request) {
			 Response<ServiceInstance> serviceInstanceResponse = getInstanceResponse(
				 serviceInstances,
				 request);
			 if (supplier instanceof SelectedInstanceCallback
				 && serviceInstanceResponse.hasServer()) {
				 ((SelectedInstanceCallback) supplier)
					 .selectedServiceInstance(serviceInstanceResponse.getServer());
			 }
			 return serviceInstanceResponse;
		 }

		 private Response<ServiceInstance> getInstanceResponse(
			 List<ServiceInstance> instances, Request request) {
			 if (instances.isEmpty()) {
				 if (log.isWarnEnabled()) {
					 log.warn("No servers available for service: " + this.serviceId);
				 }
				 return new EmptyResponse();
			 }
			 String version = VersionContextHolder.getVersion();

			 if (StrUtil.isEmpty(version)) {
				 DefaultRequestContext requestContext = (DefaultRequestContext) request.getContext();
				 RequestData clientRequest = (RequestData) requestContext.getClientRequest();
				 HttpHeaders headers = clientRequest.getHeaders();

				 version = headers.getFirst("version");
			 }

			 if (StrUtil.isEmpty(version)) {
				 return processLoadbalancerInstanceResponse(instances);
			 }

			 //取指定版本号的实例
			 String finalVersion = version;
			 List<ServiceInstance> targetList = instances
				 .stream()
				 .filter(server -> {
					 NacosServiceInstance nacosInstance = (NacosServiceInstance) server;
					 return finalVersion.equals(
						 nacosInstance.getMetadata().get(CommonConstant.METADATA_VERSION));
				 })
				 .collect(Collectors.toList());

			 if (targetList.size() > 0) {
				 return processLoadbalancerInstanceResponse(instances);
			 } else {
				 return processLoadbalancerInstanceResponse(targetList);
			 }
		 }

		 /**
		  * 负载均衡器 参考 org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer#getInstanceResponse
		  */
		 private Response<ServiceInstance> processLoadbalancerInstanceResponse(
			 List<ServiceInstance> instances) {
			 int pos = Math.abs(this.position.incrementAndGet());
			 ServiceInstance instance = instances.get(pos % instances.size());
			 return new DefaultResponse(instance);
		 }
	 }
 }
