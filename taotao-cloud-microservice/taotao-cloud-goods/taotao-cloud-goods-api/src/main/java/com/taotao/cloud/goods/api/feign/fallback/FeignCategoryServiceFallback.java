package com.taotao.cloud.goods.api.feign.fallback;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.api.feign.IFeignCategoryService;
import com.taotao.cloud.goods.api.web.vo.CategoryTreeVO;
import java.util.List;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * FeignCategoryServiceFallback
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignCategoryServiceFallback implements FallbackFactory<IFeignCategoryService> {

	@Override
	public IFeignCategoryService create(Throwable throwable) {
		return new IFeignCategoryService() {

			@Override
			public Result<List<CategoryTreeVO>> firstCategory() {
				return null;
			}
		};
	}
}
