package com.taotao.cloud.sms.loadbalancer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * load balancer interface
 *
 * @param <T> target class
 * @param <C> choose reference object
 * @author shuigedenglab
 */
public interface ILoadBalancer<T, C> {

	int MIN_WEIGHT = 1;

	/**
	 * add target object wrapper
	 *
	 * @param wrapper target object wrapper
	 */
	void addTargetWrapper(TargetWrapper<T> wrapper);

	/**
	 * add target object
	 *
	 * @param target     target object
	 * @param initActive init active status
	 */
	default void addTarget(T target, Boolean initActive) {
		if (target == null) {
			return;
		}

		TargetWrapper<T> wrapper = TargetWrapper.of(target);
		if (initActive != null) {
			wrapper.setActive(initActive);
		}
		addTargetWrapper(wrapper);
	}

	/**
	 * add target object
	 *
	 * @param target target object
	 */
	default void addTarget(T target) {
		addTarget(target, null);
	}

	/**
	 * add target object wrapper list
	 *
	 * @param wrappers target object wrapper list
	 */
	default void addTargetWrappers(Collection<TargetWrapper<T>> wrappers) {
		if (wrappers == null || wrappers.isEmpty()) {
			return;
		}

		wrappers.forEach(this::addTargetWrapper);
	}

	/**
	 * add target object list
	 *
	 * @param targets    target object list
	 * @param initActive init active status
	 */
	default void addTargets(Collection<T> targets, Boolean initActive) {
		if (targets == null || targets.isEmpty()) {
			return;
		}

		Collection<TargetWrapper<T>> wrappers = new ArrayList<>(targets.size());

		for (T target : targets) {
			if (target == null) {
				continue;
			}

			TargetWrapper<T> wrapper = TargetWrapper.of(target);
			if (initActive != null) {
				wrapper.setActive(initActive);
			}
			wrappers.add(wrapper);
		}

		addTargetWrappers(wrappers);
	}

	/**
	 * add target object list
	 *
	 * @param targets target object list
	 */
	default void addTargets(Collection<T> targets) {
		addTargets(targets, null);
	}

	/**
	 * remove target object wrapper
	 *
	 * @param wrapper target object wrapper
	 */
	void removeTargetWrapper(TargetWrapper<T> wrapper);

	/**
	 * remove target object
	 *
	 * @param target target object
	 */
	default void removeTarget(T target) {
		if (target == null) {
			return;
		}

		removeTargetWrapper(TargetWrapper.of(target));
	}

	/**
	 * remove target object wrapper list
	 *
	 * @param wrappers target object wrapper list
	 */
	default void removeTargetWrappers(Collection<TargetWrapper<T>> wrappers) {
		if (wrappers == null || wrappers.isEmpty()) {
			return;
		}

		wrappers.forEach(this::removeTargetWrapper);
	}

	/**
	 * remove target object list
	 *
	 * @param targets target object list
	 */
	default void removeTargets(Collection<T> targets) {
		if (targets == null || targets.isEmpty()) {
			return;
		}

		Collection<TargetWrapper<T>> wrappers = targets.stream().filter(Objects::nonNull)
			.map(TargetWrapper::of)
			.collect(Collectors.toList());

		removeTargetWrappers(wrappers);
	}

	/**
	 * clear target object list
	 */
	void clear();

	/**
	 * set target object weight
	 *
	 * @param target target object
	 * @param weight weight
	 */
	void setWeight(T target, int weight);

	/**
	 * select target by predicate and choose reference object, target can be null
	 *
	 * @param predicate             a <a href="package-summary.html#NonInterference">non-interfering</a>,
	 *                              <a href="package-summary.html#Statelessness">stateless</a>
	 *                              predicate to apply to each element to determine if it should be
	 *                              included
	 * @param chooseReferenceObject choose reference object
	 * @return target
	 */
	T choose(Predicate<? super T> predicate, C chooseReferenceObject);

	/**
	 * select target by choose reference object
	 *
	 * @param chooseReferenceObject choose reference object
	 * @return target
	 */
	default T choose(C chooseReferenceObject) {
		return choose(null, chooseReferenceObject);
	}

	/**
	 * select target
	 *
	 * @return target
	 */
	default T choose() {
		return choose(null, null);
	}

	/**
	 * mark target object active status is reachable
	 *
	 * @param wrapper target object wrapper
	 */
	void markReachable(TargetWrapper<T> wrapper);

	/**
	 * mark target object active status is reachable
	 *
	 * @param target target object
	 */
	default void markReachable(T target) {
		if (target == null) {
			return;
		}

		markReachable(TargetWrapper.of(target));
	}

	/**
	 * mark target object active status is down
	 *
	 * @param wrapper target object wrapper
	 */
	void markDown(TargetWrapper<T> wrapper);

	/**
	 * mark target object active status is down
	 *
	 * @param target target object
	 */
	default void markDown(T target) {
		if (target == null) {
			return;
		}

		markDown(TargetWrapper.of(target));
	}

	/**
	 * get target object wrapper list while target's active equals target object active status,
	 * returned list unsupported modify
	 *
	 * @param active target object active status
	 * @return target object wrapper list
	 */
	List<TargetWrapper<T>> getTargetWrappers(Boolean active);

	/**
	 * get target object list while target's active equals target object active status, returned
	 * list unsupported modify
	 *
	 * @param active target object active status
	 * @return target object list
	 */
	List<T> getTargets(Boolean active);
}
