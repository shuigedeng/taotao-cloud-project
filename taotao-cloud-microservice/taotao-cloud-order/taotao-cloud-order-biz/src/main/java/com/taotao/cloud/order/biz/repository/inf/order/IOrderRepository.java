package com.taotao.cloud.order.biz.repository.inf.order;

import com.taotao.cloud.order.biz.model.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 订单数据处理层
 */
public interface IOrderRepository extends JpaRepository<Order, Long> {


}
