package com.taotao.cloud.generator.api.feign.fallback;

import com.taotao.cloud.generator.api.feign.IFeignGeneratorApi;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignGeneratorFallbackImpl implements FallbackFactory<IFeignGeneratorApi> {

	@Override
	public IFeignGeneratorApi create(Throwable throwable) {
		return new IFeignGeneratorApi() {
		};
	}
}
