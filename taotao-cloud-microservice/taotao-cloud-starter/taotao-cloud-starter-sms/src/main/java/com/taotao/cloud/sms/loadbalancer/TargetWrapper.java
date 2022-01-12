package com.taotao.cloud.sms.loadbalancer;

import java.util.Objects;

/**
 * target wrapper
 *
 * @param <T> target class
 * @author shuigedeng
 */
public class TargetWrapper<T> {

	/**
	 * active status
	 */
	private boolean active;

	/**
	 * target object
	 */
	private T target;

	private TargetWrapper() {

	}

	/**
	 * build target wrapper
	 *
	 * @param target target object
	 * @param <T>    target class
	 * @return target wrapper
	 */
	public static <T> TargetWrapper<T> of(T target) {
		if (target == null) {
			throw new NullPointerException("entity is null");
		}

		TargetWrapper<T> wrapper = new TargetWrapper<T>();
		wrapper.target = target;

		return wrapper;
	}

	/**
	 * get active status
	 *
	 * @return active status
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * set active status
	 *
	 * @param active active status
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * get target object
	 *
	 * @return target object
	 */
	public T getTarget() {
		return target;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		TargetWrapper<?> that = (TargetWrapper<?>) o;
		return Objects.equals(target, that.target);
	}

	@Override
	public int hashCode() {
		return target.hashCode();
	}
}
