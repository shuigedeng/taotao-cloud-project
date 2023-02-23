package com.taotao.cloud.order.biz.repository.inf.aftersale;

import com.taotao.cloud.order.biz.model.entity.aftersale.AfterSaleLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 售后日志数据处理层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:49:21
 */
public interface IAfterSaleLogRepository extends JpaRepository<AfterSaleLog, Long> {

}