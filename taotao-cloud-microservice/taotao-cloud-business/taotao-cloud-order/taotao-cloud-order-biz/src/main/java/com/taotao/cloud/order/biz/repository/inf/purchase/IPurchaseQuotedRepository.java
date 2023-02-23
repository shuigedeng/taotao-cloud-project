package com.taotao.cloud.order.biz.repository.inf.purchase;


import com.taotao.cloud.order.biz.model.entity.purchase.PurchaseQuoted;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 采购报价数据处理层
 */
public interface IPurchaseQuotedRepository extends JpaRepository<PurchaseQuoted, Long> {

}
