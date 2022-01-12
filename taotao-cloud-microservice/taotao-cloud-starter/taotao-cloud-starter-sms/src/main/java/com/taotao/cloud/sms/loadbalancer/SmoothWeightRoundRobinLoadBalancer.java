package com.taotao.cloud.sms.loadbalancer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * smooth weight round robin Load Balancer
 *
 * @param <T> target class
 * @param <C> choose reference object
 * @author shuigedeng
 */
public class SmoothWeightRoundRobinLoadBalancer<T, C> extends AbstractLoadBalancer<T, C> {

	private final Map<T, Integer> originWeightMap = new ConcurrentHashMap<>();

	private final Map<T, Integer> currentWeightMap = new ConcurrentHashMap<>();

	private final Lock lock = new ReentrantLock();

	/**
	 * instantiation Load Balancer with CopyOnWriteArrayList
	 */
	public SmoothWeightRoundRobinLoadBalancer() {
		super();
	}

	/**
	 * instantiation Load Balancer with appoint list
	 *
	 * @param targetList target object list
	 */
	public SmoothWeightRoundRobinLoadBalancer(List<TargetWrapper<T>> targetList) {
		super(targetList);
	}

	@Override
	protected void afterAdd(TargetWrapper<T> wrapper) {
		originWeightMap.put(wrapper.getTarget(), MIN_WEIGHT);
		currentWeightMap.put(wrapper.getTarget(), MIN_WEIGHT);
	}

	@Override
	protected void afterRemove(TargetWrapper<T> wrapper) {
		originWeightMap.remove(wrapper.getTarget());
		currentWeightMap.remove(wrapper.getTarget());
	}

	@Override
	public void setWeight(T target, int weight) {
		if (target == null) {
			return;
		}

		originWeightMap.put(target, Math.max(weight, MIN_WEIGHT));
	}

	@Override
	protected T choose0(List<TargetWrapper<T>> activeTargetList, C chooseReferenceObject) {
		lock.lock();
		try {
			int originWeightSum = 0;
			int currentMaxWeight = Integer.MIN_VALUE;
			TargetWrapper<T> currentMaxWeightWrapper = null;

			for (TargetWrapper<T> wrapper : activeTargetList) {
				int originWeight = originWeightMap.getOrDefault(wrapper.getTarget(), MIN_WEIGHT);
				int currentWeight = currentWeightMap.getOrDefault(wrapper.getTarget(),
					originWeight);
				originWeightSum += originWeight;
				if (currentWeight > currentMaxWeight) {
					currentMaxWeight = currentWeight;
					currentMaxWeightWrapper = wrapper;
				}
			}

			if (currentMaxWeightWrapper == null) {
				return null;
			}

			currentWeightMap.put(currentMaxWeightWrapper.getTarget(),
				currentMaxWeight - originWeightSum);

			for (TargetWrapper<T> wrapper : activeTargetList) {
				int originWeight = originWeightMap.getOrDefault(wrapper.getTarget(), MIN_WEIGHT);
				int currentWeight = currentWeightMap.getOrDefault(wrapper.getTarget(), MIN_WEIGHT);
				currentWeightMap.put(wrapper.getTarget(), currentWeight + originWeight);
			}

			return currentMaxWeightWrapper.getTarget();
		} finally {
			lock.unlock();
		}
	}
}
