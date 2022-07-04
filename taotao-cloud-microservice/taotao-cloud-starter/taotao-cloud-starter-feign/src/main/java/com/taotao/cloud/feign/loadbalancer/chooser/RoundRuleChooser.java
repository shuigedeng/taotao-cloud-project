package com.taotao.cloud.feign.loadbalancer.chooser;

import com.taotao.cloud.common.utils.collection.CollectionUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.cloud.client.ServiceInstance;

/**
 * 轮询选择器
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-06-08 10:42:55
 */
public class RoundRuleChooser implements IRuleChooser {

	private final AtomicInteger position;

	public RoundRuleChooser() {
		this.position = new AtomicInteger(1000);
	}

	@Override
	public ServiceInstance choose(List<ServiceInstance> instances) {
		if (CollectionUtil.isNotEmpty(instances)) {
			ServiceInstance serviceInstance = instances.get(Math.abs(position.incrementAndGet() % instances.size()));
			LogUtil.info("RoundRuleChooser 选择了ip为 {}, 端口为：{} 的服务", serviceInstance.getHost(), serviceInstance.getPort());
			return serviceInstance;
		}
		return null;
	}
}
