package com.taotao.cloud.goods.api.feign.fallback;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.api.feign.IFeignCategoryApi;
import com.taotao.cloud.goods.api.model.vo.CategoryTreeVO;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * FeignCategoryServiceFallback
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignCategoryApiFallback implements FallbackFactory<IFeignCategoryApi> {

	@Override
	public IFeignCategoryApi create(Throwable throwable) {
		return new IFeignCategoryApi() {

			@Override
			public Result<List<CategoryTreeVO>> firstCategory() {
				return null;
			}
		};
	}
}
