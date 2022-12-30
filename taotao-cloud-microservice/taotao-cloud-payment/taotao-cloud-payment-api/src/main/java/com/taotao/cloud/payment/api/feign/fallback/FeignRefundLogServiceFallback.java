package com.taotao.cloud.payment.api.feign.fallback;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.payment.api.feign.IFeignRefundLogService;
import com.taotao.cloud.payment.api.vo.PayFlowVO;
import com.taotao.cloud.payment.api.vo.RefundLogVO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignRefundLogServiceFallback implements FallbackFactory<IFeignRefundLogService> {
	@Override
	public IFeignRefundLogService create(Throwable throwable) {
		return new IFeignRefundLogService() {


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
