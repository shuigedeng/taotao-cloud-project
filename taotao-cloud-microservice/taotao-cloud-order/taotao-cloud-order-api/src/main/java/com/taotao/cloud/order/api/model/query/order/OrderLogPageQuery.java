package com.taotao.cloud.order.api.model.query.order;

import com.taotao.cloud.common.enums.UserEnum;
import com.taotao.cloud.common.model.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * 订单查询参数
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "订单查询参数")
public class OrderLogPageQuery extends PageParam {

	@Serial
	private static final long serialVersionUID = -6380573339089959194L;

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


}
