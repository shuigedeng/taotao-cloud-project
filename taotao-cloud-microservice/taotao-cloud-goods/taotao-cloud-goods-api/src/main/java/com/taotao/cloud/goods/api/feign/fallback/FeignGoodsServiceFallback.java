package com.taotao.cloud.goods.api.feign.fallback;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.api.feign.IFeignCategoryService;
import com.taotao.cloud.goods.api.feign.IFeignGoodsService;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * FeignGoodsServiceFallback
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignGoodsServiceFallback implements FallbackFactory<IFeignGoodsService> {
	@Override
	public IFeignGoodsService create(Throwable throwable) {
		return new IFeignGoodsService() {
			@Override
			public Result<Boolean> updateStoreDetail(Long id) {
				return null;
			}

			@Override
			public Result<Boolean> underStoreGoods(String id) {
				return null;
			}

			@Override
			public Result<Long> countStoreGoodsNum(Long storeId) {
				return null;
			}
		};
	}
}
