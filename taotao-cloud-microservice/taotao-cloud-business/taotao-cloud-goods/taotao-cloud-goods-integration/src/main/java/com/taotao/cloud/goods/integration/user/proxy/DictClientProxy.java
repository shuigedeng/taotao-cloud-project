package com.taotao.cloud.goods.integration.user.proxy;

import com.taotao.cloud.sys.api.feign.IFeignDictApi;
import com.taotao.cloud.sys.api.feign.response.FeignDictResponse;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class DictClientProxy {
	@Resource
	private IFeignDictApi feignDictApi;

	public FeignDictResponse findByCode(String code){
		return feignDictApi.findByCode(code);
	}
}
