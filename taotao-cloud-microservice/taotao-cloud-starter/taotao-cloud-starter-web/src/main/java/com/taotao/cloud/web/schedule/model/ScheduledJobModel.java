package com.taotao.cloud.web.schedule.model;

import com.taotao.cloud.web.schedule.ScheduledException;
import com.taotao.cloud.web.schedule.enums.ScheduledType;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;

/**
 * ScheduledSource
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-09 17:16:15
 */
public class ScheduledJobModel implements Serializable {

	/**
	 * cron表达式
	 */
	private String cron;
	/**
	 * 时区，cron表达式会基于该时区解析
	 */
	private String zone;
	/**
	 * 上一次执行完毕时间点之后多长时间再执行
	 */
	private Long fixedDelay;
	/**
	 * 与 fixedDelay 意思相同，只是使用字符串的形式
	 */
	private String fixedDelayString;
	/**
	 * 上一次开始执行时间点之后多长时间再执行
	 */
	private Long fixedRate;
	/**
	 * 与 fixedRate 意思相同，只是使用字符串的形式
	 */
	private String fixedRateString;
	/**
	 * 第一次延迟多长时间后再执行
	 */
	private Long initialDelay;
	/**
	 * 与 initialDelay 意思相同，只是使用字符串的形式
	 */
	private String initialDelayString;
	/**
	 * 任务是否已终止
	 */
	private boolean cancel;
	/**
	 * 执行次数
	 */
	private int num;


	private transient Method method;

	private transient Object bean;

	private ScheduledType type;

	public Boolean check() {
		String sb = "1"
			+ (cron == null ? 0 : 1)
			+ (fixedDelay == null ? 0 : 1)
			+ (fixedDelayString == null ? 0 : 1)
			+ (fixedRate == null ? 0 : 1)
			+ (fixedRateString == null ? 0 : 1)
			+ (initialDelay == null ? 0 : 1)
			+ (initialDelayString == null ? 0 : 1);
		Integer flag = Integer.valueOf(sb, 2);
		List<Integer> probability = Arrays.asList(132, 133, 134, 136, 137, 138, 144, 145, 146, 160,
			161, 162, 192);
		return probability.contains(flag);
	}

	public ScheduledJobModel(Scheduled annotation, Method method, Object bean) {
		this.cron = StringUtils.isEmpty(annotation.cron()) ? null : annotation.cron();
		this.fixedDelay = annotation.fixedDelay() < 0 ? null : annotation.fixedDelay();
		this.fixedDelayString = StringUtils.isEmpty(annotation.fixedDelayString()) ? null
			: annotation.fixedDelayString();
		this.fixedRate = annotation.fixedRate() < 0 ? null : annotation.fixedRate();
		this.fixedRateString =
			StringUtils.isEmpty(annotation.fixedRateString()) ? null : annotation.fixedRateString();
		this.initialDelay = annotation.initialDelay() < 0 ? null : annotation.initialDelay();
		this.initialDelayString = StringUtils.isEmpty(annotation.initialDelayString()) ? null
			: annotation.initialDelayString();
		this.type = confirmType();
		this.bean = bean;
		this.method = method;
	}

	public ScheduledType confirmType() {
		if (cron != null) {
			return ScheduledType.CRON;
		} else if (fixedDelay != null || fixedDelayString != null) {
			return ScheduledType.FIXED_DELAY;
		} else if (fixedRate != null || fixedRateString != null) {
			return ScheduledType.FIXED_RATE;
		}
		return null;
	}

	public void refreshType() {
		this.type = confirmType();
		if (this.type == null) {
			throw new ScheduledException("刷新type，执行失败，无法确定定时任务的类型");
		}
	}

	public void clear() {
		this.cron = null;
		this.fixedDelay = null;
		this.fixedDelayString = null;
		this.fixedRate = null;
		this.fixedRateString = null;
	}

	public ScheduledJobModel() {
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
		refreshType();
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public Long getFixedDelay() {
		return fixedDelay;
	}

	public void setFixedDelay(Long fixedDelay) {
		this.fixedDelay = fixedDelay;
	}

	public String getFixedDelayString() {
		return fixedDelayString;
	}

	public void setFixedDelayString(String fixedDelayString) {
		this.fixedDelayString = fixedDelayString;
	}

	public Long getFixedRate() {
		return fixedRate;
	}

	public void setFixedRate(Long fixedRate) {
		this.fixedRate = fixedRate;
	}

	public String getFixedRateString() {
		return fixedRateString;
	}

	public void setFixedRateString(String fixedRateString) {
		this.fixedRateString = fixedRateString;
	}

	public Long getInitialDelay() {
		return initialDelay;
	}

	public void setInitialDelay(Long initialDelay) {
		this.initialDelay = initialDelay;
	}

	public String getInitialDelayString() {
		return initialDelayString;
	}

	public void setInitialDelayString(String initialDelayString) {
		this.initialDelayString = initialDelayString;
	}

	public ScheduledType getType() {
		return type;
	}

	public void setType(ScheduledType type) {
		this.type = type;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Object getBean() {
		return bean;
	}

	public void setBean(Object bean) {
		this.bean = bean;
	}

	public boolean isCancel() {
		return cancel;
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
}
