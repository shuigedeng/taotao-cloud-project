package com.taotao.cloud.goods.api.feign.fallback;

import com.taotao.cloud.goods.api.feign.IFeignCategoryService;
import com.taotao.cloud.goods.api.feign.IFeignEsGoodsSearchService;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * FeignEsGoodsSearchServiceFallback
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignEsGoodsSearchServiceFallback implements FallbackFactory<IFeignEsGoodsSearchService> {
	@Override
	public IFeignEsGoodsSearchService create(Throwable throwable) {
		return new IFeignEsGoodsSearchService() {

		};
	}
}
