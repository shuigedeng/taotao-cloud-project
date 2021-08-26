package com.taotao.cloud.prometheus.properties.exception;

import com.taotao.cloud.prometheus.properties.enums.ListenType;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "prometheus.exceptionnotice")
public class ExceptionNoticeProperties {

	/**
	 * 是否开启异常通知
	 */
	private boolean enabled = false;

	/**
	 * 追踪信息的包含的包名
	 */
	private String includedTracePackage;

	/**
	 * 排除的需要统计的异常
	 */
	private List<Class<? extends Exception>> excludeExceptions = new LinkedList<>();

	/**
	 * 当listenType为MVC时，处理请求异常通知时需要的header名称信息
	 */
	private List<String> includeHeaderName = new ArrayList<String>();

	/**
	 * <p>
	 * 通过注解进行监控，目前提供两种方式：
	 * </p>
	 * <ol>
	 * 一种只是普通的监视方法中的异常，主要包含了方法名、方法参数等相关内容；
	 * </ol>
	 * <ol>
	 * 另一种是监视请求出现异常后的通知，额外包含了请求路径、请求参数（param、body）以及想要查询的头信息，对于头信息的过滤参看
	 * </ol>
	 */
	private ListenType listenType = ListenType.COMMON;

	/**
	 * 是否开启异步通知
	 */
	private boolean enableAsync = false;

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the includedTracePackage
	 */
	public String getIncludedTracePackage() {
		return includedTracePackage;
	}

	/**
	 * @param includedTracePackage the includedTracePackage to set
	 */
	public void setIncludedTracePackage(String includedTracePackage) {
		this.includedTracePackage = includedTracePackage;
	}

	public List<Class<? extends Exception>> getExcludeExceptions() {
		return excludeExceptions;
	}

	public void setExcludeExceptions(List<Class<? extends Exception>> excludeExceptions) {
		this.excludeExceptions = excludeExceptions;
	}

	/**
	 * @return the includeHeaderName
	 */
	public List<String> getIncludeHeaderName() {
		return includeHeaderName;
	}

	/**
	 * @param includeHeaderName the includeHeaderName to set
	 */
	public void setIncludeHeaderName(List<String> includeHeaderName) {
		this.includeHeaderName = includeHeaderName;
	}

	/**
	 * @return the listenType
	 */
	public ListenType getListenType() {
		return listenType;
	}

	/**
	 * @param listenType the listenType to set
	 */
	public void setListenType(ListenType listenType) {
		this.listenType = listenType;
	}

	/**
	 * @return the enableAsync
	 */
	public boolean isEnableAsync() {
		return enableAsync;
	}

	/**
	 * @param enableAsync the enableAsync to set
	 */
	public void setEnableAsync(boolean enableAsync) {
		this.enableAsync = enableAsync;
	}

}
