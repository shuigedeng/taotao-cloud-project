package com.taotao.cloud.gateway.loadBalancer;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.utils.LogUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.List;
import java.util.Map;

/**
 * 基于客户端版本号灰度路由
 *
 * @author L.cm
 * @author madi
 * @date 2021-02-24 13:41
 */
@Slf4j
@AllArgsConstructor
public class VersionGrayLoadBalancer implements GrayLoadBalancer {
	private final DiscoveryClient discoveryClient;

	/**
	 * 根据serviceId 筛选可用服务
	 *
	 * @param serviceId 服务ID
	 * @param request   当前请求
	 */
	@Override
	public ServiceInstance choose(String serviceId, ServerHttpRequest request) {
		List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);

		//注册中心无实例 抛出异常
		if (CollUtil.isEmpty(instances)) {
			LogUtil.warn("No instance available for {0}", serviceId);
			throw new NotFoundException("No instance available for " + serviceId);
		}

		// 获取请求version，无则随机返回可用实例
		String reqVersion = request.getHeaders().getFirst(CommonConstant.TAOTAO_CLOUD_VERSION_HEADER);
		if (StrUtil.isBlank(reqVersion)) {
			return instances.get(RandomUtil.randomInt(instances.size()));
		}

		// 遍历可以实例元数据，若匹配则返回此实例
		for (ServiceInstance instance : instances) {
			Map<String, String> metadata = instance.getMetadata();
			String targetVersion = MapUtil.getStr(metadata, CommonConstant.TAOTAO_CLOUD_VERSION_HEADER);
			if (reqVersion.equalsIgnoreCase(targetVersion)) {
				LogUtil.debug("gray requst match success :{0} {1}", reqVersion, instance);
				return instance;
			}
		}
		return instances.get(RandomUtil.randomInt(instances.size()));
	}
}
