package com.taotao.cloud.order.api.web.vo.order;

import com.taotao.cloud.common.enums.UserEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderLogVO {


	/**
	 * 订单编号
	 */
	private String orderSn;

	/**
	 * 操作者id(可以是卖家)
	 */
	private Long operatorId;

	/**
	 * 操作者类型
	 *
	 * @see UserEnum
	 */
	private String operatorType;

	/**
	 * 操作者名称
	 */
	private String operatorName;

	/**
	 * 日志信息
	 */
	private String message;
}
