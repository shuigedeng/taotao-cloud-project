package com.taotao.cloud.order.api.feign.fallback;

import com.taotao.cloud.order.api.feign.IFeignTradeApi;
import com.taotao.cloud.order.api.model.vo.trade.TradeVO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignTradeApiFallback implements FallbackFactory<IFeignTradeApi> {

	@Override
	public IFeignTradeApi create(Throwable throwable) {
		return new IFeignTradeApi() {

			@Override
			public TradeVO getBySn(String sn) {
				return null;
			}

			@Override
			public boolean payTrade(String sn, String paymentMethod, String receivableNo) {
				return true;
			}
		};
	}
}
