package com.taotao.cloud.goods.api.feign.fallback;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.api.feign.IFeignGoodsSkuApi;
import com.taotao.cloud.goods.api.model.vo.GoodsSkuSpecGalleryVO;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * FeignGoodsSkuServiceFallback
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignGoodsSkuApiFallback implements FallbackFactory<IFeignGoodsSkuApi> {
	@Override
	public IFeignGoodsSkuApi create(Throwable throwable) {
		return new IFeignGoodsSkuApi() {
			@Override
			public Boolean updateGoodsStuck(List<GoodsSkuSpecGalleryVO> goodsSkus) {
				return null;
			}

			@Override
			public Boolean updateBatchById(List<GoodsSkuSpecGalleryVO> goodsSkus) {
				return null;
			}

			@Override
			public List<GoodsSkuSpecGalleryVO> getGoodsSkuByIdFromCache(List<Long> skuIds) {
				return null;
			}

			@Override
			public GoodsSkuSpecGalleryVO getGoodsSkuByIdFromCache(Long skuId) {
				return null;
			}

			@Override
			public Integer getStock(String skuId) {
				return null;
			}
		};
	}
}
