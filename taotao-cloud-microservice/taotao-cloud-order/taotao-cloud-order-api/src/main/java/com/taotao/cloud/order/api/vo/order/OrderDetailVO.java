package com.taotao.cloud.order.api.vo.order;


import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.taotao.cloud.order.api.enums.cart.DeliveryMethodEnum;
import com.taotao.cloud.order.api.enums.order.DeliverStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderStatusEnum;
import com.taotao.cloud.order.api.enums.order.PayStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单详情VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "订单详情VO")
public class OrderDetailVO implements Serializable {


	private static final long serialVersionUID = -6293102172184734928L;

	/**
	 * 订单
	 */
	private Order order;

	/**
	 * 子订单信息
	 */
	private List<OrderItem> orderItems;

	/**
	 * 订单状态
	 */
	private String orderStatusValue;

	/**
	 * 付款状态
	 */
	private String payStatusValue;

	/**
	 * 物流状态
	 */
	private String deliverStatusValue;

	/**
	 * 物流类型
	 */
	private String deliveryMethodValue;

	/**
	 * 支付类型
	 */
	private String paymentMethodValue;

	/**
	 * 发票
	 */
	private Receipt receipt;

	/**
	 * 获取订单日志
	 */
	private List<OrderLog> orderLogs;
	@Schema(description = "价格详情")
	private String priceDetail;

	public OrderDetailVO(Order order, List<OrderItem> orderItems, List<OrderLog> orderLogs,
		Receipt receipt) {
		this.order = order;
		this.orderItems = orderItems;
		this.orderLogs = orderLogs;
		this.receipt = receipt;
	}

	/**
	 * 可操作类型
	 */
	public AllowOperation getAllowOperationVO() {
		return new AllowOperation(this.order);
	}

	public String getOrderStatusValue() {
		try {
			return OrderStatusEnum.valueOf(order.getOrderStatus()).description();
		} catch (Exception e) {
			return "";
		}
	}

	public String getPayStatusValue() {
		try {
			return PayStatusEnum.valueOf(order.getPayStatus()).description();
		} catch (Exception e) {
			return "";
		}

	}

	public String getDeliverStatusValue() {
		try {
			return DeliverStatusEnum.valueOf(order.getDeliverStatus()).getDescription();
		} catch (Exception e) {
			return "";
		}
	}

	public String getDeliveryMethodValue() {
		try {
			return DeliveryMethodEnum.valueOf(order.getDeliveryMethod()).getDescription();
		} catch (Exception e) {
			return "";
		}
	}

	public String getPaymentMethodValue() {
		try {
			return PaymentMethodEnum.valueOf(order.getPaymentMethod()).paymentName();
		} catch (Exception e) {
			return "";
		}
	}
}
