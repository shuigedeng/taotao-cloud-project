package com.taotao.cloud.pay.alipay.ali.enums;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * 交易状态
 */
public enum TradeStatus {

	/**
	 * 成功
	 */
	SUCCESS("TRADE_SUCCESS"),
	/**
	 * 未支付
	 */
	WAIT("WAIT_BUYER_PAY"),
	/**
	 * 未付款交易超时关闭，或支付完成后全额退款
	 */
	CLOSED("TRADE_CLOSED"),
	/**
	 * 交易结束，不可退款
	 */
	FINISHED("TRADE_FINISHED"),
	/**
	 * 异常. 具体信息查询 subCode和subMsg
	 */
	ERROR(""),

	;

	private final String str;

	@JsonCreator
	public static TradeStatus of(String status) {
		if (StrUtil.isBlank(status)) {
			return ERROR;
		}

		for (var e : values()) {
			if (e.getStr().equals(status)) {
				return e;
			}
		}

		return ERROR;
	}

	TradeStatus(String str) {
		this.str = str;
	}

	public String getStr() {
		return str;
	}
}
