package com.taotao.cloud.pay.api.feign.fallback;

import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.model.Result;
import com.taotao.cloud.pay.api.feign.RemotePayFlowService;
import com.taotao.cloud.pay.api.vo.PayFlowVO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author dengtao
 * @date 2020/4/29 21:43
 */
public class RemotePayFlowFallbackImpl implements FallbackFactory<RemotePayFlowService> {
	@Override
	public RemotePayFlowService create(Throwable throwable) {
		return new RemotePayFlowService() {
			@Override
			public Result<PayFlowVO> findPayFlowById(Long id) {
				LogUtil.error("调用findPayFlowById异常：{}", throwable, id);
				return Result.failed(null, 500);
			}
		};
	}
}
