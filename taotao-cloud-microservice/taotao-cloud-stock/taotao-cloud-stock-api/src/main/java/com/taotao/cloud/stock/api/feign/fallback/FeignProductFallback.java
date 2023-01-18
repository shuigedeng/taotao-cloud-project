package com.taotao.cloud.stock.api.feign.fallback;

import com.taotao.cloud.stock.api.model.dto.ProductDTO;
import com.taotao.cloud.stock.api.feign.IFeignProductApi;
import com.taotao.cloud.stock.api.model.vo.ProductVO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignProductFallback implements FallbackFactory<IFeignProductApi> {
	@Override
	public IFeignProductApi create(Throwable throwable) {
		return new IFeignProductApi() {

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
