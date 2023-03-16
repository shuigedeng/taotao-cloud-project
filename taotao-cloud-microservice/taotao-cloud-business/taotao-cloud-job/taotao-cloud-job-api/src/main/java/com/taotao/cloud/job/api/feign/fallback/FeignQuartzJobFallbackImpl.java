package com.taotao.cloud.job.api.feign.fallback;

import com.taotao.cloud.job.api.feign.IFeignQuartzJobApi;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignQuartzJobFallbackImpl implements FallbackFactory<IFeignQuartzJobApi> {

	@Override
	public IFeignQuartzJobApi create(Throwable throwable) {
		return new IFeignQuartzJobApi() {
		};
	}
}
