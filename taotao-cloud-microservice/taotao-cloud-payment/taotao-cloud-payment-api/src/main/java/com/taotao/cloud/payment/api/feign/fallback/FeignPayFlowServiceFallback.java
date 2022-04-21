package com.taotao.cloud.payment.api.feign.fallback;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.payment.api.feign.IFeignPayFlowService;
import com.taotao.cloud.payment.api.vo.PayFlowVO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignPayFlowServiceFallback implements FallbackFactory<IFeignPayFlowService> {
	@Override
	public IFeignPayFlowService create(Throwable throwable) {
		return new IFeignPayFlowService() {

			@Override
			public Result<PayFlowVO> findPayFlowById(Long id) {
				LogUtil.error("调用findPayFlowById异常：{}", throwable, id);
				return Result.fail(null, 500);
			}
		};
	}
}
