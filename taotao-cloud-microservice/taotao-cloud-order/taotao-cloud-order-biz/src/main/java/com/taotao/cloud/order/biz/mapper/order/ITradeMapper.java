package com.taotao.cloud.order.biz.mapper.order;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.order.biz.model.entity.order.Trade;
import com.taotao.cloud.web.base.mapper.BaseSuperMapper;
import org.apache.ibatis.annotations.Update;

/**
 * 交易数据处理层
 */
public interface ITradeMapper extends BaseSuperMapper<Trade, Long> {

	/**
	 * 修改交易金额
	 *
	 * @param tradeSn 交易编号
	 */
	@Update("UPDATE tt_trade SET flow_price =(SELECT SUM(flow_price) FROM tt_order WHERE trade_sn=#{tradeSn}) WHERE sn=#{tradeSn}")
	void updateTradePrice(String tradeSn);
}
