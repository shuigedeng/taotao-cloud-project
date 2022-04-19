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
