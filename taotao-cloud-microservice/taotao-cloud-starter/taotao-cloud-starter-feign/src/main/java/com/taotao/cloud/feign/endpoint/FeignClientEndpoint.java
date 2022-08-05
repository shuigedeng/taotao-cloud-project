/*
 * MIT License
 * Copyright <2021-2022>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * @Author: Sinda
 * @Email:  xhuicloud@163.com
 */

package com.taotao.cloud.feign.endpoint;


import org.openjdk.nashorn.internal.objects.annotations.Getter;
import org.openjdk.nashorn.internal.objects.annotations.Setter;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Feign client 端点
 */
@Endpoint(id = "feignClients")
public class FeignClientEndpoint implements SmartInitializingSingleton {

	private final ApplicationContext context;

	private final List<FeignClientInfo> clientList;

	public FeignClientEndpoint(ApplicationContext context) {
		this.context = context;
		this.clientList = new ArrayList<>();
	}

	@ReadOperation
	public List<FeignClientInfo> invoke() {
		return clientList;
	}

	@Override
	public void afterSingletonsInstantiated() {
		clientList.addAll(getClientList(context));
	}

	private static List<FeignClientInfo> getClientList(ApplicationContext context) {
		Map<String, Object> feignClientMap = context.getBeansWithAnnotation(FeignClient.class);
		// 1. 解析注解
		List<FeignClientInfo> feignClientInfoList = new ArrayList<>();
		Set<Map.Entry<String, Object>> feignClientEntrySet = feignClientMap.entrySet();
		for (Map.Entry<String, Object> feignClientEntry : feignClientEntrySet) {
			String beanName = feignClientEntry.getKey();
			Object feignClientBean = feignClientEntry.getValue();
			if (feignClientBean == null) {
				continue;
			}
			// 解析注解
			Class<?> feignClientClass = feignClientBean.getClass();
			FeignClient feignClientAnn = AnnotationUtils.findAnnotation(feignClientClass, FeignClient.class);
			if (feignClientAnn == null) {
				continue;
			}
			FeignClientInfo feignClientInfo = new FeignClientInfo();
			feignClientInfo.setBeanName(beanName);
			String serviceId = feignClientAnn.value();
			String contextId = feignClientAnn.contextId();
			String url = feignClientAnn.url();
			String path = feignClientAnn.path();
			feignClientInfo.setServiceId(serviceId);
			feignClientInfo.setContextId(contextId);
			feignClientInfo.setUrl(url);
			feignClientInfo.setPath(path);
			// 组装客户端信息
			List<ClientInfo> clientInfoList = new ArrayList<>();
			Class<?>[] interfaces = feignClientClass.getInterfaces();
			for (Class<?> clientInterface : interfaces) {
				Method[] methods = clientInterface.getDeclaredMethods();
				for (Method method : methods) {
					if (method.isDefault()) {
						continue;
					}
					RequestMapping requestMapping = AnnotatedElementUtils.getMergedAnnotation(method,
							RequestMapping.class);
					if (requestMapping == null) {
						continue;
					}
					clientInfoList.add(new ClientInfo(requestMapping.method(), requestMapping.value()));
				}
			}
			feignClientInfo.setClientList(clientInfoList);
			feignClientInfoList.add(feignClientInfo);
		}
		return feignClientInfoList;
	}

	public static class FeignClientInfo {

		private String beanName;

		private String serviceId;

		private String contextId;

		private String url;

		private String path;

		private List<ClientInfo> clientList;

		public String getBeanName() {
			return beanName;
		}

		public void setBeanName(String beanName) {
			this.beanName = beanName;
		}

		public String getServiceId() {
			return serviceId;
		}

		public void setServiceId(String serviceId) {
			this.serviceId = serviceId;
		}

		public String getContextId() {
			return contextId;
		}

		public void setContextId(String contextId) {
			this.contextId = contextId;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public List<ClientInfo> getClientList() {
			return clientList;
		}

		public void setClientList(
			List<ClientInfo> clientList) {
			this.clientList = clientList;
		}
	}

	public static class ClientInfo {

		private final RequestMethod[] methods;

		private final String[] mappings;

		public ClientInfo(RequestMethod[] methods, String[] mappings) {
			this.methods = methods;
			this.mappings = mappings;
		}

		public RequestMethod[] getMethods() {
			return methods;
		}

		public String[] getMappings() {
			return mappings;
		}
	}

}
