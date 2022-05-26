package com.taotao.cloud.goods.api.feign.fallback;

import com.taotao.cloud.goods.api.feign.IFeignCategoryService;
import com.taotao.cloud.goods.api.feign.IFeignGoodsSkuService;
import com.taotao.cloud.goods.api.vo.GoodsSkuVO;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * FeignGoodsSkuServiceFallback
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignGoodsSkuServiceFallback implements FallbackFactory<IFeignGoodsSkuService> {
	@Override
	public IFeignGoodsSkuService create(Throwable throwable) {
		return new IFeignGoodsSkuService() {
			@Override
			public void updateGoodsStuck(List<GoodsSkuVO> goodsSkus) {

			}

			@Override
			public void updateBatchById(List<GoodsSkuVO> goodsSkus) {

			}

			@Override
			public List<GoodsSkuVO> getGoodsSkuByIdFromCache(List<Long> skuIds) {
				return null;
			}

			@Override
			public GoodsSkuVO getGoodsSkuByIdFromCache(Long skuId) {
				return null;
			}

			@Override
			public void getStock(String skuId) {

			}
			//@Override
			//public Result<ProductVO> findProductInfoById(Long id) {
			//	LogUtil.error("调用findProductInfoById异常：{}", id, throwable);
			//	return Result.fail(null, 500);
			//}
			//
			//@Override
			//public Result<ProductVO> saveProduct(ProductDTO productDTO) {
			//	LogUtil.error("调用saveProduct异常：{}", productDTO, throwable);
			//	return Result.fail(null, 500);
			//}
		};
	}
}
