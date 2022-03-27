package com.taotao.cloud.web.schedule.common.utils.proxy;


import com.taotao.cloud.web.schedule.core.interceptor.ScheduledRunnable;
import com.taotao.cloud.web.schedule.model.ScheduledJobModel;

/**
 * Point 
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 15:17:32
 */
public abstract class Point {

	/**
	 * 定时任务名
	 */
	private String superScheduledName;

	private ScheduledJobModel scheduledJobModel;

	/**
	 * 执行顺序
	 */
	private Integer order;

	/**
	 * 内部执行顺序
	 */
	private Integer interiorOrder;

	/**
	 * 抽象的执行方法，使用代理实现
	 *
	 * @param runnable 定时任务执行器
	 */
	public abstract Object invoke(ScheduledRunnable runnable);

	public String getSuperScheduledName() {
		return superScheduledName;
	}

	public void setSuperScheduledName(String superScheduledName) {
		this.superScheduledName = superScheduledName;
	}

	public ScheduledJobModel getScheduledSource() {
		return scheduledJobModel;
	}

	public void setScheduledSource(ScheduledJobModel scheduledJobModel) {
		this.scheduledJobModel = scheduledJobModel;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Integer getInteriorOrder() {
		return interiorOrder;
	}

	public void setInteriorOrder(Integer interiorOrder) {
		this.interiorOrder = interiorOrder;
	}
}
