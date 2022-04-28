package com.taotao.cloud.sms.loadbalancer;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * random Load Balancer
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:52:49
 */
public abstract class AbstractLoadBalancer<T, C> implements ILoadBalancer<T, C> {

	protected final List<TargetWrapper<T>> targetList;

	/**
	 * instantiation Load Balancer with CopyOnWriteArrayList
	 */
	public AbstractLoadBalancer() {
		this(new CopyOnWriteArrayList<>());
	}

	/**
	 * instantiation Load Balancer with appoint list
	 *
	 * @param targetList target object list
	 */
	public AbstractLoadBalancer(List<TargetWrapper<T>> targetList) {
		this.targetList = targetList;
	}

	@Override
	public void addTargetWrapper(TargetWrapper<T> wrapper) {
		if (wrapper == null) {
			return;
		}

		if (!targetList.contains(wrapper)) {
			targetList.add(wrapper);
			afterAdd(wrapper);
		}
	}

	protected void afterAdd(TargetWrapper<T> wrapper) {

	}

	@Override
	public void removeTargetWrapper(TargetWrapper<T> wrapper) {
		if (wrapper == null) {
			return;
		}

		targetList.remove(wrapper);
		afterRemove(wrapper);
	}

	protected void afterRemove(TargetWrapper<T> wrapper) {

	}

	@Override
	public void clear() {
		targetList.clear();
	}

	@Override
	public void setWeight(T target, int weight) {
	}

	@Override
	public T choose(Predicate<? super T> predicate, C chooseReferenceObject) {
		List<TargetWrapper<T>> activeTargetList;

		if (predicate == null) {
			activeTargetList = targetList.stream().filter(TargetWrapper::isActive)
				.collect(Collectors.toList());
		} else {
			activeTargetList = targetList.stream().filter(TargetWrapper::isActive)
				.filter(wrapper -> predicate.test(wrapper.getTarget()))
				.collect(Collectors.toList());
		}

		if (activeTargetList.isEmpty()) {
			return null;
		}

		return choose0(activeTargetList, chooseReferenceObject);
	}

	/**
	 * select target object by rule implement
	 *
	 * @param activeTargetList      active target object List
	 * @param chooseReferenceObject choose reference object
	 * @return target object
	 */
	protected abstract T choose0(List<TargetWrapper<T>> activeTargetList, C chooseReferenceObject);

	@Override
	public void markReachable(TargetWrapper<T> wrapper) {
		if (wrapper == null) {
			return;
		}

		targetList.stream().filter(wrapper::equals).forEach(item -> item.setActive(true));
	}

	@Override
	public void markDown(TargetWrapper<T> wrapper) {
		if (wrapper == null) {
			return;
		}

		targetList.stream().filter(wrapper::equals).forEach(item -> item.setActive(false));
	}

	@Override
	public List<TargetWrapper<T>> getTargetWrappers(Boolean active) {
		List<TargetWrapper<T>> wrappers;
		if (active == null) {
			wrappers = targetList;
		} else {
			wrappers = targetList.stream().filter(wrapper -> wrapper.isActive() == active)
				.collect(Collectors.toList());
		}

		return Collections.unmodifiableList(wrappers);
	}

	@Override
	public List<T> getTargets(Boolean active) {
		List<T> targets;
		if (active == null) {
			targets = targetList.stream().map(TargetWrapper::getTarget)
				.collect(Collectors.toList());
		} else {
			targets = targetList.stream().filter(wrapper -> wrapper.isActive() == active)
				.map(TargetWrapper::getTarget)
				.collect(Collectors.toList());
		}

		return Collections.unmodifiableList(targets);
	}
}
