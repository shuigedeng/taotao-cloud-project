package com.taotao.cloud.order.api.feign.fallback;

import com.taotao.cloud.order.api.feign.IFeignOrderApi;
import com.taotao.cloud.order.api.model.dto.order_info.OrderSaveDTO;
import com.taotao.cloud.order.api.model.vo.order.OrderDetailVO;
import com.taotao.cloud.order.api.model.vo.order.OrderVO;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignOrderApiFallback implements FallbackFactory<IFeignOrderApi> {

	@Override
	public IFeignOrderApi create(Throwable throwable) {
		return new IFeignOrderApi() {
			@Override
			public OrderVO findOrderInfoByCode(String code) {
				return null;
			}

			@Override
			public OrderVO saveOrder(OrderSaveDTO orderDTO) {
				return null;
			}

			@Override
			public OrderDetailVO queryDetail(String sn) {
				return null;
			}

			@Override
			public Boolean payOrder(String sn, String paymentMethod, String receivableNo) {
				return null;
			}

			@Override
			public OrderVO getBySn(String sn) {
				return null;
			}

			@Override
			public List<OrderVO> getByTradeSn(String sn) {
				return null;
			}
		};
	}
}
