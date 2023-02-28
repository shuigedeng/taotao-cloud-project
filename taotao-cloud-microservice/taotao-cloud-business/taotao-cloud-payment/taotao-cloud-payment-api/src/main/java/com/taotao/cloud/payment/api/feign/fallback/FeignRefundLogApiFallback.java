package com.taotao.cloud.payment.api.feign.fallback;

import com.taotao.cloud.payment.api.feign.IFeignRefundLogApi;
import com.taotao.cloud.payment.api.model.vo.PayFlowVO;
import com.taotao.cloud.payment.api.model.vo.RefundLogVO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignRefundLogApiFallback implements FallbackFactory<IFeignRefundLogApi> {

	@Override
	public IFeignRefundLogApi create(Throwable throwable) {
		return new IFeignRefundLogApi() {


			@Override
			public PayFlowVO findPayFlowById(Long id) {
				return null;
			}

			@Override
			public RefundLogVO queryByAfterSaleSn(String sn) {
				return null;
			}
		};
	}
}
