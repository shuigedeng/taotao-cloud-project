package com.taotao.cloud.core.properties;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.http.HttpClient;
import com.taotao.cloud.core.http.HttpClient.EnumHttpConnectParam;
import java.lang.reflect.Method;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author: chejiangyi
 * @version: 2019-07-23 17:54
 **/
@RefreshScope
@ConfigurationProperties(prefix = HttpClientProperties.PREFIX)
public class HttpClientProperties {

	public static final String PREFIX = "taotao.cloud.core.httpclient";

	/**
	 * Tcp是否粘包(批量封包发送)
	 */
	private boolean tcpNoDelay = (boolean) HttpClient.EnumHttpConnectParam.TcpNoDelay.getDefaultValue();

	/**
	 * 总连接池大小
	 */
	private int maxTotal = (int) HttpClient.EnumHttpConnectParam.MaxTotal.getDefaultValue();


	/**
	 * 单个host连接池大小
	 */
	private int defaultMaxPerRoute = (int) HttpClient.EnumHttpConnectParam.DefaultMaxPerRoute.getDefaultValue();

	/**
	 * 连接是否需要验证有效时间
	 */
	private int validateAfterInactivity = (int) HttpClient.EnumHttpConnectParam.ValidateAfterInactivity.getDefaultValue();

	/**
	 * 连接超时时间 【常用】
	 */
	private int connectTimeout = (int) HttpClient.EnumHttpConnectParam.ConnectTimeout.getDefaultValue();

	/**
	 * socket通讯超时时间 【常用】
	 */
	private int socketTimeout = (int) HttpClient.EnumHttpConnectParam.SocketTimeout.getDefaultValue();

	/**
	 * 请求从连接池获取超时时间
	 */
	private int connectionRequestTimeout = (int) HttpClient.EnumHttpConnectParam.ConnectionRequestTimeout.getDefaultValue();

	/**
	 * 连接池共享
	 */
	private boolean connectionManagerShared = (boolean) HttpClient.EnumHttpConnectParam.ConnectionManagerShared.getDefaultValue();

	/**
	 * 回收时间间隔 s
	 */
	private int evictIdleConnectionsTime = (int) HttpClient.EnumHttpConnectParam.EvictIdleConnectionsTime.getDefaultValue();

	/**
	 * 是否回收
	 */
	private boolean isEvictExpiredConnections = (boolean) HttpClient.EnumHttpConnectParam.IsEvictExpiredConnections.getDefaultValue();

	/**
	 * 长连接保持时间 s
	 */
	private int connectionTimeToLive = (int) HttpClient.EnumHttpConnectParam.ConnectionTimeToLive.getDefaultValue();

	/**
	 * 重试次数 【常用】
	 */
	private int retryCount = (int) HttpClient.EnumHttpConnectParam.RetryCount.getDefaultValue();

	public static HttpClient.InitMap toMap() {
		HttpClient.InitMap initMap = new HttpClient.InitMap();
		for (Method m : HttpClientProperties.class.getMethods()) {
			try {
				EnumHttpConnectParam en = HttpClient.EnumHttpConnectParam.get(m.getName());
				if (en != null) {
					initMap.trySetDefaultParams(en, m.invoke(null));
				}
			} catch (Exception e) {

			}
		}
		return initMap;
	}


	public boolean isTcpNoDelay() {
		return tcpNoDelay;
	}

	public void setTcpNoDelay(boolean tcpNoDelay) {
		this.tcpNoDelay = tcpNoDelay;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public int getDefaultMaxPerRoute() {
		return defaultMaxPerRoute;
	}

	public void setDefaultMaxPerRoute(int defaultMaxPerRoute) {
		this.defaultMaxPerRoute = defaultMaxPerRoute;
	}

	public int getValidateAfterInactivity() {
		return validateAfterInactivity;
	}

	public void setValidateAfterInactivity(int validateAfterInactivity) {
		this.validateAfterInactivity = validateAfterInactivity;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public int getConnectionRequestTimeout() {
		return connectionRequestTimeout;
	}

	public void setConnectionRequestTimeout(int connectionRequestTimeout) {
		this.connectionRequestTimeout = connectionRequestTimeout;
	}

	public boolean isConnectionManagerShared() {
		return connectionManagerShared;
	}

	public void setConnectionManagerShared(boolean connectionManagerShared) {
		this.connectionManagerShared = connectionManagerShared;
	}

	public int getEvictIdleConnectionsTime() {
		return evictIdleConnectionsTime;
	}

	public void setEvictIdleConnectionsTime(int evictIdleConnectionsTime) {
		this.evictIdleConnectionsTime = evictIdleConnectionsTime;
	}

	public boolean isEvictExpiredConnections() {
		return isEvictExpiredConnections;
	}

	public void setEvictExpiredConnections(boolean evictExpiredConnections) {
		isEvictExpiredConnections = evictExpiredConnections;
	}

	public int getConnectionTimeToLive() {
		return connectionTimeToLive;
	}

	public void setConnectionTimeToLive(int connectionTimeToLive) {
		this.connectionTimeToLive = connectionTimeToLive;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}
}
