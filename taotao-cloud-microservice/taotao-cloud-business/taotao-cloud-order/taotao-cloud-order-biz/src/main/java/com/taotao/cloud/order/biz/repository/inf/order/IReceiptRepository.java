package com.taotao.cloud.order.biz.repository.inf.order;

import com.taotao.cloud.order.biz.model.entity.order.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 发票数据处理层
 */
public interface IReceiptRepository extends JpaRepository<Receipt, Long> {
}
