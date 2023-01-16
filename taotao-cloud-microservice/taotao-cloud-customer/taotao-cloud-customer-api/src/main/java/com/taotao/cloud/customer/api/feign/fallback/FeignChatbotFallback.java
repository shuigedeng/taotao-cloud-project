package com.taotao.cloud.customer.api.feign.fallback;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.customer.api.feign.IFeignChatbotService;
import com.taotao.cloud.customer.api.web.vo.ChatbotVO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignChatbotFallback implements FallbackFactory<IFeignChatbotService> {

	@Override
	public IFeignChatbotService create(Throwable throwable) {
		return new IFeignChatbotService() {

			@Override
			public Result<ChatbotVO> findChatbotById(Long id) {
				LogUtils.error("调用getMemberSecurityUser异常：{}", throwable, id);
				return Result.fail(null, 500);
			}
		};
	}
}
