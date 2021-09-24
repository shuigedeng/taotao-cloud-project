package com.taotao.cloud.mail.api.feign.fallback;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.mail.api.feign.RemoteEmailService;
import com.taotao.cloud.mail.api.vo.EmailVO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class RemoteEmailFallbackImpl implements FallbackFactory<RemoteEmailService> {
	@Override
	public RemoteEmailService create(Throwable throwable) {
		return new RemoteEmailService() {
			@Override
			public Result<EmailVO> findEmailById(Long id) {
				LogUtil.error("调用findEmailById异常：{}", throwable, id);
				return Result.fail(null, 500);
			}
		};
	}
}
