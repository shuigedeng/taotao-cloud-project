package com.taotao.cloud.prometheus.properties;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "prometheus.service-monitor")
public class ServiceMonitorProperties implements InitializingBean {

	/**
	 * 是否开启服务检查
	 */
	private boolean enabled = false;

	/**
	 * 需要监控的服务
	 */
	private Map<String, ServiceCheck> monitorServices = new HashMap<>();

	/**
	 * 微服务存在性检查间隔 ， 默认两分钟
	 */
	private Duration serviceExistCheckInterval = Duration.ofMinutes(2);

	/**
	 * 服务检查通知的时间间隔，默认3分钟
	 */
	private Duration serviceCheckNoticeInterval = Duration.ofMinutes(3);

	/**
	 * 刷新全部通知的时间间隔，默认半小时
	 * 
	 */
	private Duration refreshServiceCheckNoticeInterval = Duration.ofMinutes(30);

	/**
	 * 是否自动发现服务，默认为true
	 */
	private boolean autoDiscovery = true;

	public Map<String, ServiceCheck> getMonitorServices() {
		return monitorServices;
	}

	public void setMonitorServices(Map<String, ServiceCheck> monitorServices) {
		this.monitorServices = monitorServices;
	}

	public Duration getServiceExistCheckInterval() {
		return serviceExistCheckInterval;
	}

	public void setServiceExistCheckInterval(Duration serviceExistCheckInterval) {
		this.serviceExistCheckInterval = serviceExistCheckInterval;
	}

	public Duration getServiceCheckNoticeInterval() {
		return serviceCheckNoticeInterval;
	}

	public void setServiceCheckNoticeInterval(Duration serviceCheckNoticeInterval) {
		this.serviceCheckNoticeInterval = serviceCheckNoticeInterval;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isAutoDiscovery() {
		return autoDiscovery;
	}

	public void setAutoDiscovery(boolean autoDiscovery) {
		this.autoDiscovery = autoDiscovery;
	}

	/**
	 * @return the refreshServiceCheckNoticeInterval
	 */
	public Duration getRefreshServiceCheckNoticeInterval() {
		return refreshServiceCheckNoticeInterval;
	}

	/**
	 * @param refreshServiceCheckNoticeInterval the
	 *                                          refreshServiceCheckNoticeInterval to
	 *                                          set
	 */
	public void setRefreshServiceCheckNoticeInterval(Duration refreshServiceCheckNoticeInterval) {
		this.refreshServiceCheckNoticeInterval = refreshServiceCheckNoticeInterval;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		monitorServices.putIfAbsent("default", new ServiceCheck());
	}

}
