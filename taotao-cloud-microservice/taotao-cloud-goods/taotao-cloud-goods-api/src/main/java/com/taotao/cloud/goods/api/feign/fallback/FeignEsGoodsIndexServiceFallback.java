package com.taotao.cloud.goods.api.feign.fallback;

import com.taotao.cloud.goods.api.feign.IFeignCategoryService;
import com.taotao.cloud.goods.api.feign.IFeignEsGoodsIndexService;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * FeignEsGoodsIndexServiceFallback
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignEsGoodsIndexServiceFallback implements FallbackFactory<IFeignEsGoodsIndexService> {
	@Override
	public IFeignEsGoodsIndexService create(Throwable throwable) {
		return new IFeignEsGoodsIndexService() {
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
