package com.taotao.cloud.product.api.feign.fallback;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.product.api.dto.ProductDTO;
import com.taotao.cloud.product.api.feign.RemoteProductService;
import com.taotao.cloud.product.api.vo.ProductVO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class RemoteProductFallbackImpl implements FallbackFactory<RemoteProductService> {
	@Override
	public RemoteProductService create(Throwable throwable) {
		return new RemoteProductService() {
			@Override
			public Result<ProductVO> findProductInfoById(Long id) {
				LogUtil.error("调用findProductInfoById异常：{}", id, throwable);
				return Result.fail(null, 500);
			}

			@Override
			public Result<ProductVO> saveProduct(ProductDTO productDTO) {
				LogUtil.error("调用saveProduct异常：{}", productDTO, throwable);
				return Result.fail(null, 500);
			}
		};
	}
}
