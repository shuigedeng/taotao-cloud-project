package com.taotao.cloud.goods.integration.user.proxy;

import com.taotao.cloud.sys.api.feign.DictApi;
import com.taotao.cloud.sys.api.feign.response.DictApiResponse;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class DictClientProxy {
	@Resource
	private DictApi feignDictApi;

	public DictApiResponse findByCode(String code){
		return feignDictApi.findByCode(code);
	}
}
