package com.taotao.cloud.goods.api.feign.fallback;

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
