package com.taotao.cloud.sys.api.feign.fallback;

import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.sys.api.feign.IFeignDictService;
import com.taotao.cloud.sys.api.feign.response.FeignDictRes;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * FeignDictFallback
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignDictFallback implements FallbackFactory<IFeignDictService> {
	@Override
	public IFeignDictService create(Throwable throwable) {
		LogUtil.info("throwablethrowablethrowablethrowablethrowable");

		return new IFeignDictService() {

			@Override
			public FeignDictRes findByCode(String code) {
				return null;
			}
		};
	}
}
