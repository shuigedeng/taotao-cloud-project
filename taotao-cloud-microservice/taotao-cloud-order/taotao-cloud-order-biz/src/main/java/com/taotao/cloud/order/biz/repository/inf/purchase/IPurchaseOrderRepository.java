package com.taotao.cloud.order.biz.repository.inf.purchase;


import com.taotao.cloud.order.biz.model.entity.purchase.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 采购单数据处理层
 */
public interface IPurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {


}
