package com.taotao.cloud.order.biz.repository.inf.order;

import com.taotao.cloud.order.biz.model.entity.order.OrderComplaintCommunication;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 交易投诉通信数据处理层
 */
public interface IOrderComplainCommunicationRepository extends JpaRepository<OrderComplaintCommunication, Long> {

}
