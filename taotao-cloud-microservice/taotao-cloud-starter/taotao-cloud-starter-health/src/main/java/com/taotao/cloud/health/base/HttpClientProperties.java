package com.taotao.cloud.health.base;

import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.PropertyUtil;
import com.taotao.cloud.health.base.HttpClient.EnumHttpConnectParam;
import java.lang.reflect.Method;

/**
 * @author: chejiangyi
 * @version: 2019-07-23 17:54
 **/
public class HttpClientProperties {

	public static String Prefix = "bsf.httpclient.";

	/**
	 * Tcp是否粘包(批量封包发送)
	 */
	public static boolean tcpNoDelay() {
		return PropertyUtil.getPropertyCache("bsf.httpclient.tcpNoDelay",
			(boolean) HttpClient.EnumHttpConnectParam.TcpNoDelay.getDefaultValue());
	}

	/**
	 * 总连接池大小
	 */
	public static int maxTotal() {
		return PropertyUtil.getPropertyCache("bsf.httpclient.maxTotal",
			(int) HttpClient.EnumHttpConnectParam.MaxTotal.getDefaultValue());
	}

	/**
	 * 单个host连接池大小
	 */
	public static int defaultMaxPerRoute() {
		return PropertyUtil.getPropertyCache("bsf.httpclient.defaultMaxPerRoute",
			(int) HttpClient.EnumHttpConnectParam.DefaultMaxPerRoute.getDefaultValue());
	}

	/**
	 * 连接是否需要验证有效时间
	 */
	public static int validateAfterInactivity() {
		return PropertyUtil.getPropertyCache("bsf.httpclient.validateAfterInactivity",
			(int) HttpClient.EnumHttpConnectParam.ValidateAfterInactivity.getDefaultValue());
	}

	/**
	 * 连接超时时间 【常用】
	 */
	public static int connectTimeout() {
		return PropertyUtil.getPropertyCache("bsf.httpclient.connectTimeout",
			(int) HttpClient.EnumHttpConnectParam.ConnectTimeout.getDefaultValue());
	}

	/**
	 * socket通讯超时时间 【常用】
	 */
	public static int socketTimeout() {
		return PropertyUtil.getPropertyCache("bsf.httpclient.socketTimeout",
			(int) HttpClient.EnumHttpConnectParam.SocketTimeout.getDefaultValue());
	}

	/**
	 * 请求从连接池获取超时时间
	 */
	public static int connectionRequestTimeout() {
		return PropertyUtil.getPropertyCache("bsf.httpclient.connectionRequestTimeout",
			(int) HttpClient.EnumHttpConnectParam.ConnectionRequestTimeout.getDefaultValue());
	}

	/**
	 * 连接池共享
	 */
	public static boolean connectionManagerShared() {
		return PropertyUtil.getPropertyCache("bsf.httpclient.connectionManagerShared",
			(boolean) HttpClient.EnumHttpConnectParam.ConnectionManagerShared.getDefaultValue());
	}

	/**
	 * 回收时间间隔 s
	 */
	public static int evictIdleConnectionsTime() {
		return PropertyUtil.getPropertyCache("bsf.httpclient.evictIdleConnectionsTime",
			(int) HttpClient.EnumHttpConnectParam.EvictIdleConnectionsTime.getDefaultValue());
	}

	/**
	 * 是否回收
	 */
	public static boolean isEvictExpiredConnections() {
		return PropertyUtil.getPropertyCache("bsf.httpclient.isEvictExpiredConnections",
			(boolean) HttpClient.EnumHttpConnectParam.IsEvictExpiredConnections.getDefaultValue());
	}

	/**
	 * 长连接保持时间 s
	 */
	public static int connectionTimeToLive() {
		return PropertyUtil.getPropertyCache("bsf.httpclient.connectionTimeToLive",
			(int) HttpClient.EnumHttpConnectParam.ConnectionTimeToLive.getDefaultValue());
	}

	/**
	 * 重试次数 【常用】
	 */
	public static int retryCount() {
		return PropertyUtil.getPropertyCache("bsf.httpclient.retryCount",
			(int) HttpClient.EnumHttpConnectParam.RetryCount.getDefaultValue());
	}

	public static HttpClient.InitMap toMap() {
		HttpClient.InitMap initMap = new HttpClient.InitMap();
		for (Method m : HttpClientProperties.class.getMethods()) {
			try {
				EnumHttpConnectParam en = HttpClient.EnumHttpConnectParam.get(m.getName());
				if (en != null) {
					initMap.trySetDefaultParams(en, m.invoke(null));
				}
			} catch (Exception e) {
				LogUtil.error(CoreProperties.Project, "httpclient toMap", e);
			}
		}
		return initMap;
	}

	public static String getPrefix() {
		return Prefix;
	}

	public static void setPrefix(String prefix) {
		Prefix = prefix;
	}
}
