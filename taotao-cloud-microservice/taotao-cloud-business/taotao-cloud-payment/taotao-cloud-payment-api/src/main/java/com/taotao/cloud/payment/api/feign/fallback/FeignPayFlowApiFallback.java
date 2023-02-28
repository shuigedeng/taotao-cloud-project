package com.taotao.cloud.payment.api.feign.fallback;

import com.taotao.cloud.payment.api.feign.IFeignPayFlowApi;
import com.taotao.cloud.payment.api.model.vo.PayFlowVO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignPayFlowApiFallback implements FallbackFactory<IFeignPayFlowApi> {

	@Override
	public IFeignPayFlowApi create(Throwable throwable) {
		return new IFeignPayFlowApi() {

			@Override
			public PayFlowVO findPayFlowById(Long id) {
				return null;
			}
		};
	}
}
