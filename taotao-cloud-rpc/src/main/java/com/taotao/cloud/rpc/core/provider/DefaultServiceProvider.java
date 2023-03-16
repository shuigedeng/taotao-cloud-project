package com.taotao.cloud.rpc.core.provider;

import com.taotao.cloud.rpc.common.exception.ServiceNotFoundException;
import com.taotao.cloud.rpc.common.exception.ServiceNotImplException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultServiceProvider implements ServiceProvider {

	// 存放服务名-服务对象键值对，不需要实例化，保证全局唯一
	private static final Map<String, Object> serviceMap = new ConcurrentHashMap<>();
	// 存放已注册的 服务对象 对应 key值，不需要实例化，保证全局唯一
	private static final Set<String> registeredService = ConcurrentHashMap.newKeySet();

	@Override
	public synchronized <T> void addServiceProvider(T service, String serviceName)
		throws ServiceNotImplException {
		// 服务名
		if (registeredService.contains(serviceName)) {
			return;
		}
		registeredService.add(serviceName);
		// 获取该 对象的 所有接口 对象
		// 将 service 所有暴露的接口名 以键值对 存入 Map（暴露给客户端的只有接口）
		serviceMap.put(serviceName, service);
		log.info("Register service: {} with interface: {} ", service.getClass().getInterfaces(),
			serviceName);
	}

	@Override
	public synchronized Object getServiceProvider(String serviceName)
		throws ServiceNotFoundException {
		Object service = serviceMap.get(serviceName);
		log.debug("getServiceProvider - service [{}]", service);
		if (service == null) {
			throw new ServiceNotFoundException("Service Not Found Exception!");
		}
		return service;
	}

}
