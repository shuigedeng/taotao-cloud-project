package com.taotao.cloud.feign.loadbalancer.chooser;

import java.util.List;
import org.springframework.cloud.client.ServiceInstance;

/**
 * service选择器类
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-06-08 10:42:05
 */
public interface IRuleChooser {

	/**
	 * @param instances
	 * @return {@link ServiceInstance }
	 * @since 2022-06-08 10:42:16
	 */
	ServiceInstance choose(List<ServiceInstance> instances);
}
