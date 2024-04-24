package com.taotao.cloud.auth.adapter.facade;

import com.taotao.cloud.auth.api.feign.IFeignClientApi;
import com.taotao.cloud.auth.api.feign.request.FeignClientQueryRequest;
import com.taotao.cloud.auth.api.feign.response.FeignClientResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 为远程客户端提供粗粒度的调用接口
 */
@Validated
@RestController
@RequestMapping("/sys/feign/dict")
public class FeignClientApi implements IFeignClientApi {


	@Override
	public FeignClientResponse query(FeignClientQueryRequest feignClientQueryRequest) {
		return null;
	}
}
