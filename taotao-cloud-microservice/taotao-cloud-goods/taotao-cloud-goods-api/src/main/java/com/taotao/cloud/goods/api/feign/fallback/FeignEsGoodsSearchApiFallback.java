package com.taotao.cloud.goods.api.feign.fallback;

import com.taotao.cloud.goods.api.feign.IFeignEsGoodsSearchApi;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * FeignEsGoodsSearchServiceFallback
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignEsGoodsSearchApiFallback implements FallbackFactory<IFeignEsGoodsSearchApi> {
	@Override
	public IFeignEsGoodsSearchApi create(Throwable throwable) {
		return new IFeignEsGoodsSearchApi() {

		};
	}
}
