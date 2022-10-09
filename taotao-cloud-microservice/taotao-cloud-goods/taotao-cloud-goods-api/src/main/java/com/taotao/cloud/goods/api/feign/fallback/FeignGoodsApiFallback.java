package com.taotao.cloud.goods.api.feign.fallback;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.api.feign.IFeignGoodsApi;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * FeignGoodsServiceFallback
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignGoodsApiFallback implements FallbackFactory<IFeignGoodsApi> {
	@Override
	public IFeignGoodsApi create(Throwable throwable) {
		return new IFeignGoodsApi() {
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
