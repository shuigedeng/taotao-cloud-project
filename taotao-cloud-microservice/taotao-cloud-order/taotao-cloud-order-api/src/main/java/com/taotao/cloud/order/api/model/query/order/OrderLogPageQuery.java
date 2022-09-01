package com.taotao.cloud.order.api.model.query.order;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.taotao.cloud.common.enums.UserEnum;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.order.api.enums.order.CommentStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderPromotionTypeEnum;
import com.taotao.cloud.order.api.enums.order.OrderStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderTagEnum;
import com.taotao.cloud.order.api.enums.order.OrderTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.time.LocalDateTime;

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
