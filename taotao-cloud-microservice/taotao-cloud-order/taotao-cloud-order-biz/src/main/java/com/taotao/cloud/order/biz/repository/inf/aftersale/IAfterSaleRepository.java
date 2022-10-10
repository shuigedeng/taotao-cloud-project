package com.taotao.cloud.order.biz.repository.inf.aftersale;

import com.taotao.cloud.order.biz.model.entity.aftersale.AfterSale;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 售后数据处理层
 *
 * @author shuigedeng
 */
public interface IAfterSaleRepository extends JpaRepository<AfterSale, Long> {

}
