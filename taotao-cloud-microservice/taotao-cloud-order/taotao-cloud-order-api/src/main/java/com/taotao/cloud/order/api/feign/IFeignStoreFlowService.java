package com.taotao.cloud.order.api.feign;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.order.api.feign.fallback.FeignTradeFallbackImpl;
import com.taotao.cloud.order.api.query.order.StoreFlowPageQuery;
import com.taotao.cloud.order.api.vo.order.StoreFlowVO;
import com.taotao.cloud.order.api.vo.trade.TradeVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 远程调用订单模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(value = ServiceName.TAOTAO_CLOUD_ORDER, fallbackFactory = FeignTradeFallbackImpl.class)
public interface IFeignStoreFlowService {

	@GetMapping(value = "/trade")
	TradeVO getBySn(String sn);

	IPage<StoreFlowVO> getStoreFlow(String id, String flowType, PageParam pageParam);

	IPage<StoreFlowVO> getDistributionFlow(String id, PageParam pageParam);
}

