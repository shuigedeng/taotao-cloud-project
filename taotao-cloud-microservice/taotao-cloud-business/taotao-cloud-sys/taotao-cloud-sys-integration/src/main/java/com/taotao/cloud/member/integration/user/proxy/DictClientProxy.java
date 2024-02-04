package com.taotao.cloud.member.integration.user.proxy;

import com.taotao.cloud.sys.api.feign.IFeignDictApi;
import com.taotao.cloud.sys.api.feign.IFeignUserApi;
import com.taotao.cloud.sys.api.feign.response.FeignDictResponse;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

@Component
public class DictClientProxy {
	@Resource
	private IFeignDictApi feignDictApi;

	public FeignDictResponse findByCode(String code){
		return feignDictApi.findByCode(code);
	}
}
