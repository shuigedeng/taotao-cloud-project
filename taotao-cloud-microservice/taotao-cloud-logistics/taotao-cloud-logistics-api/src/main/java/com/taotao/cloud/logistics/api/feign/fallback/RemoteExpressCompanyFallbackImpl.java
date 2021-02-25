package com.taotao.cloud.logistics.api.feign.fallback;

import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.model.Result;
import com.taotao.cloud.logistics.api.feign.RemoteExpressCompanyService;
import com.taotao.cloud.logistics.api.vo.ExpressCompanyVO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author dengtao
 * @date 2020/4/29 21:43
 */
public class RemoteExpressCompanyFallbackImpl implements FallbackFactory<RemoteExpressCompanyService> {
	@Override
	public RemoteExpressCompanyService create(Throwable throwable) {
		return new RemoteExpressCompanyService() {
			@Override
			public Result<ExpressCompanyVO> findExpressCompanyById(Long id) {
				LogUtil.error("调用findExpressCompanyById异常：{}", throwable, id);
				return Result.failed(null, 500);
			}
		};
	}
}
