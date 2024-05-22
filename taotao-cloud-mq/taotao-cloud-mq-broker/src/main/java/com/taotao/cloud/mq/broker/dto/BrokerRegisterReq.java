package com.taotao.cloud.mq.broker.dto;


import com.taotao.cloud.mq.common.dto.req.MqCommonReq;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class BrokerRegisterReq extends MqCommonReq {

	/**
	 * 服务信息
	 */
	private ServiceEntry serviceEntry;

	/**
	 * 账户标识
	 *
	 * @since 2024.05
	 */
	private String appKey;

	/**
	 * 账户密码
	 *
	 * @since 2024.05
	 */
	private String appSecret;

	public ServiceEntry getServiceEntry() {
		return serviceEntry;
	}

	public void setServiceEntry(ServiceEntry serviceEntry) {
		this.serviceEntry = serviceEntry;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	@Override
	public String toString() {
		return "BrokerRegisterReq{" +
			"serviceEntry=" + serviceEntry +
			", appKey='" + appKey + '\'' +
			", appSecret='" + appSecret + '\'' +
			"} " + super.toString();
	}

}
