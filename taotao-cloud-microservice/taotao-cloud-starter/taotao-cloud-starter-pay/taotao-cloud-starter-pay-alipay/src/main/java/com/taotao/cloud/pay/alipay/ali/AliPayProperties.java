package com.taotao.cloud.pay.alipay.ali;

/**
 * @author lingting 2021/1/25 11:29
 */
public class AliPayProperties {

	/**
	 * 是否使用沙箱, 如果为 false 则使用 prod的配置初始化支付信息, 否则使用dev配置进行初始化
	 */
	private Boolean sandbox = false;

	/**
	 * 线上环境配置
	 */
	private Config prod;

	/**
	 * 沙箱配置
	 */
	private Config dev;

	public static class Config {

		private String appId;

		/**
		 * rsa私钥(应用私钥)
		 */
		private String privateKey;

		private String format = "json";

		private String charset = "utf-8";

		/**
		 * 支付宝公钥
		 */
		private String alipayPublicKey;

		private String signType = "RSA2";

		private String returnUrl;

		private String notifyUrl;

		public String getAppId() {
			return appId;
		}

		public void setAppId(String appId) {
			this.appId = appId;
		}

		public String getPrivateKey() {
			return privateKey;
		}

		public void setPrivateKey(String privateKey) {
			this.privateKey = privateKey;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public String getCharset() {
			return charset;
		}

		public void setCharset(String charset) {
			this.charset = charset;
		}

		public String getAlipayPublicKey() {
			return alipayPublicKey;
		}

		public void setAlipayPublicKey(String alipayPublicKey) {
			this.alipayPublicKey = alipayPublicKey;
		}

		public String getSignType() {
			return signType;
		}

		public void setSignType(String signType) {
			this.signType = signType;
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

	public Boolean getSandbox() {
		return sandbox;
	}

	public void setSandbox(Boolean sandbox) {
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
}
