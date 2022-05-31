package com.taotao.cloud.order.api.feign.fallback;

import com.taotao.cloud.order.api.feign.IFeignTradeService;
import com.taotao.cloud.order.api.vo.trade.TradeVO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignTradeFallbackImpl implements FallbackFactory<IFeignTradeService> {

	@Override
	public IFeignTradeService create(Throwable throwable) {
		return new IFeignTradeService() {

			@Override
			public TradeVO getBySn(String sn) {
				return null;
			}
		};
	}
}
