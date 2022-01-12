package com.taotao.cloud.sms.loadbalancer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * weight round robin Load Balancer
 *
 * @param <T> target class
 * @param <C> choose reference object
 * @author shuigedeng
 */
public class WeightRoundRobinLoadBalancer<T, C> extends AbstractLoadBalancer<T, C> {

	private final Map<T, Integer> weightMap = new ConcurrentHashMap<>();

	private final Lock lock = new ReentrantLock();

	private int position = 0;

	/**
	 * instantiation Load Balancer with CopyOnWriteArrayList
	 */
	public WeightRoundRobinLoadBalancer() {
		super();
	}

	/**
	 * instantiation Load Balancer with appoint list
	 *
	 * @param targetList target object list
	 */
	public WeightRoundRobinLoadBalancer(List<TargetWrapper<T>> targetList) {
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

		int size = targetList.size();

		lock.lock();
		try {
			if (position >= size) {
				position = 0;
			}
			TargetWrapper<T> wrapper = targetList.get(position);
			position++;

			return wrapper == null ? null : wrapper.getTarget();
		} finally {
			lock.unlock();
		}
	}
}
