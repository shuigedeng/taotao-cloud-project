package com.taotao.cloud.logistics.api.feign;

import com.taotao.cloud.common.constant.ServiceNameConstant;
import com.taotao.cloud.core.model.Result;
import com.taotao.cloud.logistics.api.feign.fallback.RemoteExpressCompanyFallbackImpl;
import com.taotao.cloud.logistics.api.vo.ExpressCompanyVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 远程调用快递公司模块
 *
 * @author dengtao
 * @date 2020/5/2 16:42
 */
@FeignClient(contextId = "remoteExpressCompanyService", value = ServiceNameConstant.TAOTAO_CLOUD_LOGISTICS_CENTER, fallbackFactory = RemoteExpressCompanyFallbackImpl.class)
public interface RemoteExpressCompanyService {

	/**
	 * 根据id查询会员登录日志信息
	 *
	 * @param id id
	 * @return com.taotao.cloud.core.model.Result<ChatbotVO>
	 * @author dengtao
	 * @date 2020/11/20 上午10:45
	 * @since v1.0
	 */
	@GetMapping("/express/company/info/id/{id:[0-9]*}")
	Result<ExpressCompanyVO> findExpressCompanyById(@PathVariable(value = "id") Long id);
}

