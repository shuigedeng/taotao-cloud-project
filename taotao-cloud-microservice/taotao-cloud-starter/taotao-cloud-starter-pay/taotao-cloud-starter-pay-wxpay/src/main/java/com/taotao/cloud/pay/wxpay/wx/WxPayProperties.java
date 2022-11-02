package com.taotao.cloud.pay.wxpay.wx;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lingting 2021/1/25 11:29
 */
@ConfigurationProperties(prefix = "ballcat.pay.wx")
public class WxPayProperties {

	/**
	 * 是否使用沙箱, 如果为 false 则使用 prod的配置初始化支付信息, 否则使用dev配置进行初始化
	 */
	private boolean sandbox = false;

	/**
	 * 线上环境配置
	 */
	private Config prod;

	/**
	 * 沙箱配置.
	 */
	private Config dev;

	public boolean isSandbox() {
		return sandbox;
	}

	public void setSandbox(boolean sandbox) {
		this.sandbox = sandbox;
	}

	public Config getProd() {
		return prod;
	}

	public void setProd(Config prod) {
		this.prod = prod;
	}

	public Config getDev() {
		return dev;
	}

	public void setDev(Config dev) {
		this.dev = dev;
	}

	public static class Config {

		private String appId;

		private String mchId;

		private String mckKey;

		private String returnUrl;

		private String notifyUrl;

		public String getAppId() {
			return appId;
		}

		public void setAppId(String appId) {
			this.appId = appId;
		}

		public String getMchId() {
			return mchId;
		}

		public void setMchId(String mchId) {
			this.mchId = mchId;
		}

		public String getMckKey() {
			return mckKey;
		}

		public void setMckKey(String mckKey) {
			this.mckKey = mckKey;
		}

		public String getReturnUrl() {
			return returnUrl;
		}

		public void setReturnUrl(String returnUrl) {
			this.returnUrl = returnUrl;
		}

		public String getNotifyUrl() {
			return notifyUrl;
		}

		public void setNotifyUrl(String notifyUrl) {
			this.notifyUrl = notifyUrl;
		}
	}

}
