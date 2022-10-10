package com.taotao.cloud.order.biz.repository.inf.purchase;


import com.taotao.cloud.order.biz.model.entity.purchase.PurchaseQuotedItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 采购单子内容数据处理层
 */
public interface IPurchaseQuotedItemRepository extends JpaRepository<PurchaseQuotedItem, Long> {


}
