package com.taotao.cloud.order.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.order.api.feign.fallback.FeignTradeFallbackImpl;
import com.taotao.cloud.order.api.web.vo.trade.TradeVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 远程调用订单模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(value = ServiceName.TAOTAO_CLOUD_ORDER, fallbackFactory = FeignTradeFallbackImpl.class)
public interface IFeignTradeService {

	@GetMapping(value = "/trade")
	TradeVO getBySn(String sn);

    void payTrade(String sn, String paymentMethod, String receivableNo);

}

