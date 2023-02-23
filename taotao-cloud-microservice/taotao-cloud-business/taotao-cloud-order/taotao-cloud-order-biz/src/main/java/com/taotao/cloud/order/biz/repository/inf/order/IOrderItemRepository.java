package com.taotao.cloud.order.biz.repository.inf.order;

import com.taotao.cloud.order.biz.model.entity.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 子订单数据处理层
 */
public interface IOrderItemRepository extends JpaRepository<OrderItem, Long> {
}
