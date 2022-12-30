package com.taotao.cloud.order.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.PageQuery;
import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.order.api.feign.fallback.FeignTradeApiFallback;
import com.taotao.cloud.order.api.model.vo.order.StoreFlowVO;
import com.taotao.cloud.order.api.model.vo.trade.TradeVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 远程调用订单模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(value = ServiceName.TAOTAO_CLOUD_ORDER, fallbackFactory = FeignTradeApiFallback.class)
public interface IFeignStoreFlowApi {

	@GetMapping(value = "/trade")
	TradeVO getBySn(String sn);

	PageResult<StoreFlowVO> getStoreFlow(String id, String flowType, PageQuery PageQuery);

	PageResult<StoreFlowVO> getDistributionFlow(String id, PageQuery PageQuery);
}

