package com.taotao.cloud.goods.integration.user.proxy;

import com.taotao.cloud.sys.api.feign.DictApi;
import com.taotao.cloud.sys.api.feign.response.FeignDictResponse;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class DictClientProxy {
	@Resource
	private DictApi feignDictApi;

	public FeignDictResponse findByCode(String code){
		return feignDictApi.findByCode(code);
	}
}
