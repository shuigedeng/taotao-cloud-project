package com.taotao.cloud.order.api.vo.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单支付日志：实际为订单部分字段提取过来的一个vo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "订单支付日志：实际为订单部分字段提取过来的一个vo")
public class PaymentLog extends BaseEntity {


	private static final long serialVersionUID = 2233811628066468683L;
	@ApiModelProperty("订单编号")
	private String sn;

	@ApiModelProperty("交易编号 关联Trade")
	private String tradeSn;

	@Schema(description = "店铺ID")
	private String storeId;

	@Schema(description = "店铺名称")
	private String storeName;

	@Schema(description = "会员ID")
	private String memberId;

	@Schema(description = "用户名")
	private String memberName;

	/**
	 * @see PayStatusEnum
	 */
	@Schema(description = "付款状态")
	private String payStatus;

	@Schema(description = "第三方付款流水号")
	private String receivableNo;

	@Schema(description = "支付方式")
	private String paymentMethod;

	@Schema(description = "支付时间")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date paymentTime;


	@Schema(description = "总价格")
	private Double flowPrice;

	@Schema(description = "支付方式返回的交易号")
	private String payOrderNo;

	/**
	 * @see ClientTypeEnum
	 */
	@Schema(description = "订单来源")
	private String clientType;

	/**
	 * @see OrderTypeEnum
	 */
	@Schema(description = "订单类型")
	private String orderType;

}
