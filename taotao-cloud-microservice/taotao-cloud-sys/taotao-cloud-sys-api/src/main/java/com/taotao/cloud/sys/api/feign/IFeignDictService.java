package com.taotao.cloud.sys.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.sys.api.feign.fallback.FeignDictServiceFallback;
import com.taotao.cloud.sys.api.feign.response.FeignDictRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用后台用户模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(name = ServiceName.TAOTAO_CLOUD_SYS, fallbackFactory = FeignDictServiceFallback.class)
public interface IFeignDictService {

	/**
	 * 字典列表code查询
	 *
	 * @param code 代码
	 * @return {@link FeignDictRes }
	 * @since 2022-06-29 21:40:21
	 */
	@GetMapping("/sys/remote/dict/code")
	FeignDictRes findByCode(@RequestParam(value = "code") String code);

}

