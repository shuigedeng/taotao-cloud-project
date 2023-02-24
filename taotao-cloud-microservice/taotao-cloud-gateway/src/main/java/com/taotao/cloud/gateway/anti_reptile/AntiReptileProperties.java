package com.taotao.cloud.gateway.anti_reptile;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
@ConfigurationProperties(prefix = AntiReptileProperties.PREFIX)
public class AntiReptileProperties {

	public static final String PREFIX = "taotao.cloud.gateway.anti.reptile";

	/**
	 * 是否启用反爬虫插件
	 */
	private boolean enabled = false;

	/**
	 * 是否启用全局拦截，默认为false，可设置为true全局拦截
	 */
	private boolean globalFilterMode = false;

	/**
	 * 非全局拦截下，需要反爬的接口列表，以'/'开头，以','分隔
	 */
	private List<String> includeUrls;

	/**
	 * 基于请求IP的反爬规则
	 */
	private IpRule ipRule = new IpRule();

	/**
	 * 基于请求User-Agent的反爬规则
	 */
	private UaRule uaRule = new UaRule();

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<String> getIncludeUrls() {
		return includeUrls;
	}

	public void setIncludeUrls(List<String> includeUrls) {
		this.includeUrls = includeUrls;
	}

	public IpRule getIpRule() {
		return ipRule;
	}

	public void setIpRule(IpRule ipRule) {
		this.ipRule = ipRule;
	}

	public UaRule getUaRule() {
		return uaRule;
	}

	public void setUaRule(UaRule uaRule) {
		this.uaRule = uaRule;
	}

	public boolean isGlobalFilterMode() {
		return globalFilterMode;
	}

	public void setGlobalFilterMode(boolean globalFilterMode) {
		this.globalFilterMode = globalFilterMode;
	}

	public static class IpRule {

		/**
		 * 是否启用IP Rule：默认启用
		 */
		private boolean enabled = true;

		/**
		 * 时间窗口：默认5000ms
		 */
		private Integer expirationTime = 5000;

		/**
		 * 最大请求数，默认20
		 */
		private Integer requestMaxSize = 20;

		/**
		 * 命中规则后，锁定期限,默认10天，单位：秒（s）
		 */
		private long lockExpire = TimeUnit.DAYS.toSeconds(1);
		/**
		 * IP白名单，支持后缀'*'通配，以','分隔
		 */
		private List<String> ignoreIp;

		public long getLockExpire() {
			return lockExpire;
		}

		public void setLockExpire(long lockExpire) {
			this.lockExpire = lockExpire;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public Integer getExpirationTime() {
			return expirationTime;
		}

		public void setExpirationTime(Integer expirationTime) {
			this.expirationTime = expirationTime;
		}

		public Integer getRequestMaxSize() {
			return requestMaxSize;
		}

		public void setRequestMaxSize(Integer requestMaxSize) {
			this.requestMaxSize = requestMaxSize;
		}

		public List<String> getIgnoreIp() {
			return ignoreIp;
		}

		public void setIgnoreIp(List<String> ignoreIp) {
			this.ignoreIp = ignoreIp;
		}
	}

	public static class UaRule {

		/**
		 * 是否启用User-Agent Rule：默认启用
		 */
		private boolean enabled = true;

		/**
		 * 是否允许Linux系统访问：默认否
		 */
		private boolean allowedLinux = false;

		/**
		 * 是否允许移动端设备访问：默认是
		 */
		private boolean allowedMobile = true;

		/**
		 * 是否允许移PC设备访问: 默认是
		 */
		private boolean allowedPc = true;

		/**
		 * 是否允许Iot设备访问：默认否
		 */
		private boolean allowedIot = false;

		/**
		 * 是否允许代理访问：默认否
		 */
		private boolean allowedProxy = false;

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public boolean isAllowedLinux() {
			return allowedLinux;
		}

		public void setAllowedLinux(boolean allowedLinux) {
			this.allowedLinux = allowedLinux;
		}

		public boolean isAllowedMobile() {
			return allowedMobile;
		}

		public void setAllowedMobile(boolean allowedMobile) {
			this.allowedMobile = allowedMobile;
		}

		public boolean isAllowedPc() {
			return allowedPc;
		}

		public void setAllowedPc(boolean allowedPc) {
			this.allowedPc = allowedPc;
		}

		public boolean isAllowedIot() {
			return allowedIot;
		}

		public void setAllowedIot(boolean allowedIot) {
			this.allowedIot = allowedIot;
		}

		public boolean isAllowedProxy() {
			return allowedProxy;
		}

		public void setAllowedProxy(boolean allowedProxy) {
			this.allowedProxy = allowedProxy;
		}
	}
}
