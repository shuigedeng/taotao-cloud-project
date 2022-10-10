package com.taotao.cloud.order.biz.repository.inf.order;

import com.taotao.cloud.order.biz.model.entity.order.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 交易数据处理层
 */
public interface ITradeRepository extends JpaRepository<Trade, Long> {

}
