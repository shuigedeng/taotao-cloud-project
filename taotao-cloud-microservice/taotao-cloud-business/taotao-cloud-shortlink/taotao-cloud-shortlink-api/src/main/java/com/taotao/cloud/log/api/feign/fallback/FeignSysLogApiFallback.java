package com.taotao.cloud.log.api.feign.fallback;


import com.taotao.cloud.log.api.feign.IFeignSysLogApi;
import com.taotao.cloud.log.api.feign.request.FeignSysLogRequest;
import com.taotao.cloud.log.api.feign.response.FeignLogsResponse;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * FeignDictFallback
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignSysLogApiFallback implements FallbackFactory<IFeignSysLogApi> {


	@Override
	public IFeignSysLogApi create(Throwable cause) {
		return new IFeignSysLogApi() {
			@Override
			public FeignLogsResponse save(FeignSysLogRequest feignSysLogRequest) {
				return null;
			}
		};
	}
}
