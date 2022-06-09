package com.taotao.cloud.sms.common.loadbalancer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * weight random Load Balancer
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:53:14
 */
public class WeightRandomLoadBalancer<T, C> extends AbstractLoadBalancer<T, C> {

	private final Map<T, Integer> weightMap = new ConcurrentHashMap<>();

	/**
	 * instantiation Load Balancer with CopyOnWriteArrayList
	 */
	public WeightRandomLoadBalancer() {
		super();
	}

	/**
	 * instantiation Load Balancer with appoint list
	 *
	 * @param targetList target object list
	 */
	public WeightRandomLoadBalancer(List<TargetWrapper<T>> targetList) {
		super(targetList);
	}

	@Override
	protected void afterAdd(TargetWrapper<T> wrapper) {
		weightMap.put(wrapper.getTarget(), MIN_WEIGHT);
	}

	@Override
	protected void afterRemove(TargetWrapper<T> wrapper) {
		weightMap.remove(wrapper.getTarget());
	}

	@Override
	public void setWeight(T target, int weight) {
		if (target == null) {
			return;
		}

		weightMap.put(target, Math.max(weight, MIN_WEIGHT));
	}

	@Override
	protected T choose0(List<TargetWrapper<T>> activeTargetList, C chooseReferenceObject) {
		List<TargetWrapper<T>> targetList = new ArrayList<>();

		for (TargetWrapper<T> wrapper : activeTargetList) {
			int weight = weightMap.getOrDefault(wrapper.getTarget(), MIN_WEIGHT);
			for (int i = 0; i < weight; i++) {
				targetList.add(wrapper);
			}
		}

		TargetWrapper<T> wrapper = targetList.get(new Random().nextInt(targetList.size()));
		return wrapper == null ? null : wrapper.getTarget();
	}
}
