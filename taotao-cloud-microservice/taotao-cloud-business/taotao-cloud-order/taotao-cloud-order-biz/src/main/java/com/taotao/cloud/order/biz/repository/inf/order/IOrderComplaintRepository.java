package com.taotao.cloud.order.biz.repository.inf.order;

import com.taotao.cloud.order.biz.model.entity.order.OrderComplaint;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 订单投诉数据处理层
 */
public interface IOrderComplaintRepository extends JpaRepository<OrderComplaint, Long> {
}
