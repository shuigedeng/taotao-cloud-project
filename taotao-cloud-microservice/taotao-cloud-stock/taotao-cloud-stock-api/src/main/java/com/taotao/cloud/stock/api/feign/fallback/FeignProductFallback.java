package com.taotao.cloud.stock.api.feign.fallback;

import com.taotao.cloud.stock.api.dto.ProductDTO;
import com.taotao.cloud.stock.api.feign.IFeignProductService;
import com.taotao.cloud.stock.api.vo.ProductVO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignProductFallback implements FallbackFactory<IFeignProductService> {
	@Override
	public IFeignProductService create(Throwable throwable) {
		return new IFeignProductService() {

			@Override
			public ProductVO findProductInfoById(Long id) {
				return null;
			}

			@Override
			public ProductVO saveProduct(ProductDTO productDTO) {
				return null;
			}
		};
	}
}
