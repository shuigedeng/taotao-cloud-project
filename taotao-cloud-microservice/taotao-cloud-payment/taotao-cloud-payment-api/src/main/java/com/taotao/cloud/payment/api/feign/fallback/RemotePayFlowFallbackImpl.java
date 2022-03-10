package com.taotao.cloud.dubbo.api.feign.fallback;

import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.core.model.Result;
import com.taotao.cloud.dubbo.api.feign.RemotePayFlowService;
import com.taotao.cloud.dubbo.api.vo.PayFlowVO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
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
