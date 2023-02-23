package com.taotao.cloud.order.biz.repository.inf.aftersale;

import com.taotao.cloud.order.biz.model.entity.aftersale.AfterSaleReason;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 售后原因数据处理层
 */
public interface IAfterSaleReasonRepository extends JpaRepository<AfterSaleReason, Long> {


}
