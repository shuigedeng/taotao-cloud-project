package com.taotao.cloud.log.api.feign.fallback;


import com.taotao.cloud.log.api.feign.IFeignSysLogLoginApi;
import com.taotao.cloud.log.api.feign.request.FeignSysLogLoginRequest;
import com.taotao.cloud.log.api.feign.response.FeignLogsResponse;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * FeignDictFallback
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignSysLogLoginApiFallback implements FallbackFactory<IFeignSysLogLoginApi> {


	@Override
	public IFeignSysLogLoginApi create(Throwable cause) {
		return new IFeignSysLogLoginApi() {
			@Override
			public FeignLogsResponse save(FeignSysLogLoginRequest feignSysLogLoginRequest) {
				return null;
			}
		};
	}
}
