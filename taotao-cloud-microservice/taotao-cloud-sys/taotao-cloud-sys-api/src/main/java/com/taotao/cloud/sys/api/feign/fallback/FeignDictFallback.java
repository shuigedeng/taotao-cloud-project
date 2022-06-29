package com.taotao.cloud.sys.api.feign.fallback;

import com.taotao.cloud.sys.api.feign.IFeignDictService;
import com.taotao.cloud.sys.api.feign.response.DictResponse;
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
		return new IFeignDictService() {

			@Override
			public DictResponse findByCode(String code) {
				return null;
			}
		};
	}
}
