package com.taotao.cloud.mail.api.feign;

import com.taotao.cloud.common.constant.ServiceNameConstant;
import com.taotao.cloud.core.model.Result;
import com.taotao.cloud.mail.api.feign.fallback.RemoteEmailFallbackImpl;
import com.taotao.cloud.mail.api.vo.EmailVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 远程调用快递公司模块
 *
 * @author dengtao
 * @date 2020/5/2 16:42
 */
@FeignClient(contextId = "remoteEmailService", value = ServiceNameConstant.TAOTAO_CLOUD_LOGISTICS_CENTER, fallbackFactory = RemoteEmailFallbackImpl.class)
public interface RemoteEmailService {

	/**
	 * 根据id查询邮件信息
	 *
	 * @param id id
	 * @return com.taotao.cloud.core.model.Result<ChatbotVO>
	 * @author dengtao
	 * @date 2020/11/20 上午10:45
	 * @since v1.0
	 */
	@GetMapping("/email/info/id/{id:[0-9]*}")
	public Result<EmailVO> findEmailById(@PathVariable(value = "id") Long id);
}

