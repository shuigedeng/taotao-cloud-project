package com.taotao.cloud.order.api.vo.order;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 订单vo
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "订单vo")
public class OrderVO extends OrderBaseVO {

	private static final long serialVersionUID = 5820637554656388777L;

	@Schema(description = "订单商品项目")
	private List<OrderItem> orderItems;

	public OrderVO(OrderBaseVO order, List<OrderItem> orderItems) {
		BeanUtil.copyProperties(order, this);
		this.setOrderItems(orderItems);
	}
}
