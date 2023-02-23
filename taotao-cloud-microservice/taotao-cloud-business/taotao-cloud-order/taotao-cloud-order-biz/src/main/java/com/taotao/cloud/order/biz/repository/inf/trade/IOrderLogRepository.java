package com.taotao.cloud.order.biz.repository.inf.trade;

import com.taotao.cloud.order.biz.model.entity.order.OrderLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 订单日志数据处理层
 */
public interface IOrderLogRepository extends JpaRepository<OrderLog, Long> {

}
