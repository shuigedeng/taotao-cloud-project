package com.taotao.cloud.sys.biz.entity.config.payment;

import java.util.List;
import lombok.Data;

/**
 * 支持的支付方式属性
 */
@Data
public class PaymentSupportItem {

	/**
	 * 客户端 h5/app/小程序/pc
	 */
	private String client;

	/**
	 * 支持的支付方式
	 */
	private List<String> supports;

}
