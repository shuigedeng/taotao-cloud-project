package com.taotao.cloud.sms.loadbalancer;

import java.util.List;
import java.util.Random;

/**
 * random Load Balancer
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:52:59
 */
public class RandomLoadBalancer<T, C> extends AbstractLoadBalancer<T, C> {

	/**
	 * instantiation Load Balancer with CopyOnWriteArrayList
	 */
	public RandomLoadBalancer() {
		super();
	}

	/**
	 * instantiation Load Balancer with appoint list
	 *
	 * @param targetList target object list
	 */
	public RandomLoadBalancer(List<TargetWrapper<T>> targetList) {
		super(targetList);
	}

	@Override
	protected T choose0(List<TargetWrapper<T>> activeTargetList, C chooseReferenceObject) {
		TargetWrapper<T> wrapper = activeTargetList.get(
			new Random().nextInt(activeTargetList.size()));
		return wrapper == null ? null : wrapper.getTarget();
	}
}
