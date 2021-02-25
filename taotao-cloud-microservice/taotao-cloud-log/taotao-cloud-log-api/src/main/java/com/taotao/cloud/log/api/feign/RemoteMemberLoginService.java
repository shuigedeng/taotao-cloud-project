package com.taotao.cloud.log.api.feign;

import com.taotao.cloud.common.constant.ServiceNameConstant;
import com.taotao.cloud.core.model.Result;
import com.taotao.cloud.log.api.feign.fallback.RemoteMemberLoginFallbackImpl;
import com.taotao.cloud.log.api.vo.MemberLoginVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * RemoteMemberLoginService
 *
 * @author dengtao
 * @date 2020/11/27 下午3:06
 * @since v1.0
 */
@FeignClient(contextId = "remoteMemberLoginService", value = ServiceNameConstant.TAOTAO_CLOUD_AFTERSALE_CENTER, fallbackFactory = RemoteMemberLoginFallbackImpl.class)
public interface RemoteMemberLoginService {

	/**
	 * 根据id查询会员登录日志信息
	 *
	 * @param id id
	 * @return com.taotao.cloud.core.model.Result<ChatbotVO>
	 * @author dengtao
	 * @date 2020/11/20 上午10:45
	 * @since v1.0
	 */
	@GetMapping("/member/login/info/id/{id:[0-9]*}")
	public Result<MemberLoginVO> findMemberLoginById(@PathVariable(value = "id") Long id);
}

