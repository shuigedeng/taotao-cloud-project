package com.taotao.cloud.sms.loadbalancer;

import java.util.List;
import java.util.Random;

/**
 * hash Load Balancer
 *
 * @param <T> target class
 * @param <C> choose reference object
 * @author shuigedeng
 */
public class HashLoadBalancer<T, C> extends AbstractLoadBalancer<T, C> {

	/**
	 * instantiation Load Balancer with CopyOnWriteArrayList
	 */
	public HashLoadBalancer() {
		super();
	}

	/**
	 * instantiation Load Balancer with appoint list
	 *
	 * @param targetList target object list
	 */
	public HashLoadBalancer(List<TargetWrapper<T>> targetList) {
		super(targetList);
	}

	@Override
	protected T choose0(List<TargetWrapper<T>> activeTargetList, C chooseReferenceObject) {
		if (chooseReferenceObject == null) {
			Random random = new Random();

			TargetWrapper<T> wrapper = activeTargetList.get(
				random.nextInt(activeTargetList.size()));

			return wrapper == null ? null : wrapper.getTarget();
		} else {
			int hashCode = chooseReferenceObject.hashCode();
			int size = activeTargetList.size();
			int position = hashCode % size;

			TargetWrapper<T> wrapper = activeTargetList.get(position);

			return wrapper == null ? null : wrapper.getTarget();
		}
	}
}
