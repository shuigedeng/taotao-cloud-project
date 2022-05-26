package com.taotao.cloud.order.api.vo.order;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 订单vo
 */
@Schema(description = "订单vo")
public record OrderVO(

	@Schema(description = "订单商品项目")
	List<OrderItem> orderItems,

	OrderBaseVO orderBaseVO
) {

	@Serial
	private static final long serialVersionUID = 8808470688518188146L;


	public OrderVO(OrderBaseVO order, List<OrderItem> orderItems) {
		BeanUtil.copyProperties(order, this);
		this.setOrderItems(orderItems);
	}
}
