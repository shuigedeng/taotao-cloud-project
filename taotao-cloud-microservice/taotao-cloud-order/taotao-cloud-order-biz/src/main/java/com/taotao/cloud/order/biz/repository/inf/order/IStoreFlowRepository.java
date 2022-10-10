package com.taotao.cloud.order.biz.repository.inf.order;

import com.taotao.cloud.order.biz.model.entity.order.StoreFlow;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 商家订单流水数据处理层
 */
public interface IStoreFlowRepository extends JpaRepository<StoreFlow, Long> {

}
