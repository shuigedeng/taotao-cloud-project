package com.taotao.cloud.flowable.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.flowable.api.feign.fallback.FeignChatbotFallback;
import com.taotao.cloud.flowable.api.model.vo.ChatbotVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 远程调用售后模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(contextId = "remoteChatbotService", value = ServiceName.TAOTAO_CLOUD_FLOWABLE, fallbackFactory = FeignChatbotFallback.class)
public interface IFeignChatbotService {

	/**
	 * 根据id查询机器人客服信息o
	 *
	 * @param id id
	 * @return com.taotao.cloud.core.model.Result<ChatbotVO>
	 * @author shuigedeng
	 * @version 2022.03
	 * @since 2020/11/20 上午10:45
	 */
	@GetMapping("/chatbot/info/id/{id:[0-9]*}")
	Result<ChatbotVO> findChatbotById(@PathVariable(value = "id") Long id);
}

