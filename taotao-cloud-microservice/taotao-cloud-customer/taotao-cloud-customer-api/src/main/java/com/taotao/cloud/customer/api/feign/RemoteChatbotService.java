package com.taotao.cloud.customer.api.feign;

import com.taotao.cloud.common.constant.ServiceNameConstant;
import com.taotao.cloud.core.model.Result;
import com.taotao.cloud.customer.api.feign.fallback.RemoteChatbotFallbackImpl;
import com.taotao.cloud.customer.api.vo.ChatbotVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 远程调用售后模块
 *
 * @author dengtao
 * @date 2020/5/2 16:42
 */
@FeignClient(contextId = "remoteChatbotService", value = ServiceNameConstant.TAOTAO_CLOUD_AFTERSALE_CENTER, fallbackFactory = RemoteChatbotFallbackImpl.class)
public interface RemoteChatbotService {

	/**
	 * 根据id查询机器人客服信息o
	 *
	 * @param id id
	 * @return com.taotao.cloud.core.model.Result<ChatbotVO>
	 * @author dengtao
	 * @date 2020/11/20 上午10:45
	 * @since v1.0
	 */
	@GetMapping("/chatbot/info/id/{id:[0-9]*}")
	Result<ChatbotVO> findChatbotById(@PathVariable(value = "id") Long id);
}

