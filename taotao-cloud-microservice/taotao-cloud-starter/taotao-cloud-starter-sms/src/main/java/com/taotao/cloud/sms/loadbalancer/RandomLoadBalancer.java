package com.taotao.cloud.sms.loadbalancer;

import java.util.List;
import java.util.Random;

/**
 * random Load Balancer
 *
 * @param <T> target class
 * @param <C> choose reference object
 * @author shuigedeng
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
