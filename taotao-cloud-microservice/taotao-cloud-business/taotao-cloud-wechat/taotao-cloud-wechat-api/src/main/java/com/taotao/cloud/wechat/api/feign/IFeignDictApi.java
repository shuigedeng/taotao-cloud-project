package com.taotao.cloud.wechat.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.wechat.api.feign.fallback.FeignDictApiFallback;
import com.taotao.cloud.wechat.api.feign.response.FeignDictResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用后台用户模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(name = ServiceName.TAOTAO_CLOUD_SYS, contextId = "feignDictApi", fallbackFactory = FeignDictApiFallback.class)
public interface IFeignDictApi {

	/**
	 * 字典列表code查询
	 *
	 * @param code 代码
	 * @return {@link FeignDictResponse }
	 * @since 2022-06-29 21:40:21
	 */
	@GetMapping("/sys/remote/dict/code")
	FeignDictResponse findByCode(@RequestParam(value = "code") String code);

}

