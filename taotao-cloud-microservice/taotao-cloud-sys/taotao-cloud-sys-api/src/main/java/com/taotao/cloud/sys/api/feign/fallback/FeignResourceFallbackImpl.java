package com.taotao.cloud.sys.api.feign.fallback;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.sys.api.feign.IFeignResourceService;
import com.taotao.cloud.sys.api.vo.resource.ResourceQueryVO;
import java.util.List;
import java.util.Set;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignResourceFallbackImpl implements FallbackFactory<IFeignResourceService> {
	@Override
	public IFeignResourceService create(Throwable throwable) {
		return new IFeignResourceService() {
			@Override
			public Result<List<ResourceQueryVO>> findResourceByCodes(Set<String> codes) {
				LogUtil.error("调用findResourceByCodes异常：{}", throwable, codes);
				return Result.fail(null, 500);
			}
		};
	}
}
