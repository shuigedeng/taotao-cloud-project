package com.taotao.cloud.order.api.vo.order;

import cn.hutool.core.bean.BeanUtil;
import cn.lili.modules.order.order.entity.dos.Order;
import cn.lili.modules.order.order.entity.dos.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

/**
 * 订单vo
 */
@Data
@Schema(description = "订单vo")
public class OrderVO extends Order {


	private static final long serialVersionUID = 5820637554656388777L;

	@Schema(description = "订单商品项目")
	private List<OrderItem> orderItems;


	public OrderVO(Order order, List<OrderItem> orderItems) {
		BeanUtil.copyProperties(order, this);
		this.setOrderItems(orderItems);
	}
}
