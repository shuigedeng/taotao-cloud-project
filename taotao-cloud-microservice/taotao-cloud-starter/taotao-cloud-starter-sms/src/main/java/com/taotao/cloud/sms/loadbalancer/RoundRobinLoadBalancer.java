package com.taotao.cloud.sms.loadbalancer;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * round robin Load Balancer
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:53:03
 */
public class RoundRobinLoadBalancer<T, C> extends AbstractLoadBalancer<T, C> {

	private final Lock lock = new ReentrantLock();

	private int position = 0;

	/**
	 * instantiation Load Balancer with CopyOnWriteArrayList
	 */
	public RoundRobinLoadBalancer() {
		super();
	}

	/**
	 * instantiation Load Balancer with appoint list
	 *
	 * @param targetList target object list
	 */
	public RoundRobinLoadBalancer(List<TargetWrapper<T>> targetList) {
		super(targetList);
	}

	@Override
	protected T choose0(List<TargetWrapper<T>> activeTargetList, C chooseReferenceObject) {
		int size = activeTargetList.size();
		lock.lock();
		try {
			if (position >= size) {
				position = 0;
			}
			TargetWrapper<T> wrapper = activeTargetList.get(position);
			position++;

			return wrapper == null ? null : wrapper.getTarget();
		} finally {
			lock.unlock();
		}
	}
}
