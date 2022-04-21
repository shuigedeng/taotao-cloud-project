package com.taotao.cloud.stream.framework.trigger.message;


/**
 * 拼团订单延时任务信息
 */
public class PintuanOrderMessage {

	/**
	 * 拼团活动id
	 */
	private String pintuanId;

	/**
	 * 父拼团订单sn
	 */
	private String orderSn;

	public String getPintuanId() {
		return pintuanId;
	}

	public void setPintuanId(String pintuanId) {
		this.pintuanId = pintuanId;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}
}
