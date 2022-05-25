package com.taotao.cloud.payment.api.feign.fallback;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.payment.api.feign.IFeignPayFlowService;
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
			public Result<PayFlowVO> findPayFlowById(Long id) {
				LogUtil.error("调用findPayFlowById异常：{}", throwable, id);
				return Result.fail(null, 500);
			}

			@Override
			public RefundLogVO queryByAfterSaleSn(String sn) {
				return null;
			}
		};
	}
}
