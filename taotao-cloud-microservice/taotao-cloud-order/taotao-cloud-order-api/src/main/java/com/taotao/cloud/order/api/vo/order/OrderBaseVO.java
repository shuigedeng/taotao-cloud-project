package com.taotao.cloud.order.api.vo.order;

import com.taotao.cloud.common.enums.ClientTypeEnum;
import com.taotao.cloud.order.api.enums.cart.DeliveryMethodEnum;
import com.taotao.cloud.order.api.enums.order.DeliverStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderPromotionTypeEnum;
import com.taotao.cloud.order.api.enums.order.OrderStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderTypeEnum;
import com.taotao.cloud.order.api.enums.order.PayStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:01:19
 */
@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderBaseVO implements Serializable {
	@Serial
	private static final long serialVersionUID = 8808470688518188146L;

	/**
	 * 订单编号
	 */
	private String sn;

	/**
	 * 交易编号 关联Trade
	 */
	private String tradeSn;

	/**
	 * 店铺ID
	 */
	private Long storeId;

	/**
	 * 店铺名称
	 */
	private String storeName;

	/**
	 * 会员ID
	 */
	private Long memberId;

	/**
	 * 用户名
	 */
	private String memberName;

	/**
	 * 订单状态
	 *
	 * @see OrderStatusEnum
	 */
	private String orderStatus;

	/**
	 * 付款状态
	 *
	 * @see PayStatusEnum
	 */
	private String payStatus;

	/**
	 * 货运状态
	 *
	 * @see DeliverStatusEnum
	 */
	private String deliverStatus;

	/**
	 * 第三方付款流水号
	 */
	private String receivableNo;

	/**
	 * 支付方式
	 */
	private String paymentMethod;

	/**
	 * 支付时间
	 */
	private LocalDateTime paymentTime;

	/**
	 * 收件人姓名
	 */
	private String consigneeName;

	/**
	 * 收件人手机
	 */
	private String consigneeMobile;

	/**
	 * 配送方式
	 *
	 * @see DeliveryMethodEnum
	 */
	private String deliveryMethod;

	/**
	 * 地址名称， ','分割
	 */
	private String consigneeAddressPath;

	/**
	 * 地址id，','分割
	 */
	private String consigneeAddressIdPath;

	/**
	 * 详细地址
	 */
	private String consigneeDetail;

	/**
	 * 总价格
	 */
	private BigDecimal flowPrice;

	/**
	 * 商品价格
	 */
	private BigDecimal goodsPrice;

	/**
	 * 运费
	 */
	private BigDecimal freightPrice;

	/**
	 * 优惠的金额
	 */
	private BigDecimal discountPrice;

	/**
	 * 修改价格
	 */
	private BigDecimal updatePrice;

	/**
	 * 发货单号
	 */
	private String logisticsNo;

	/**
	 * 物流公司CODE
	 */
	private String logisticsCode;

	/**
	 * 物流公司名称
	 */
	private String logisticsName;

	/**
	 * 订单商品总重量
	 */
	private BigDecimal weight;

	/**
	 * 商品数量
	 */
	private Integer goodsNum;

	/**
	 * 买家订单备注
	 */
	private String remark;

	/**
	 * 订单取消原因
	 */
	private String cancelReason;

	/**
	 * 完成时间
	 */
	private LocalDateTime completeTime;

	/**
	 * 送货时间
	 */
	private LocalDateTime logisticsTime;

	/**
	 * 支付方式返回的交易号
	 */
	private String payOrderNo;

	/**
	 * 订单来源
	 *
	 * @see ClientTypeEnum
	 */
	private String clientType;

	/**
	 * 是否需要发票
	 */
	private Boolean needReceipt;

	/**
	 * 是否为其他订单下的订单，如果是则为依赖订单的sn，否则为空
	 */
	private String parentOrderSn;

	/**
	 * 是否为某订单类型的订单，如果是则为订单类型的id，否则为空
	 */
	private String promotionId;

	/**
	 * 订单类型
	 *
	 * @see OrderTypeEnum
	 */
	private String orderType;

	/**
	 * 订单促销类型
	 *
	 * @see OrderPromotionTypeEnum
	 */
	private String orderPromotionType;

	/**
	 * 价格详情
	 */
	private String priceDetail;

	/**
	 * 订单是否支持原路退回
	 */
	private Boolean canReturn;

	/**
	 * 提货码
	 */
	private String verificationCode;

	/**
	 * 分销员ID
	 */
	private Long distributionId;

	/**
	 * 使用的店铺会员优惠券id(,区分)
	 */
	private String useStoreMemberCouponIds;

	/**
	 * 使用的平台会员优惠券id
	 */
	private String usePlatformMemberCouponId;
}
