package com.taotao.cloud.uc.api.feign.fallback;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.uc.api.feign.RemoteResourceService;
import com.taotao.cloud.uc.api.vo.resource.ResourceVO;
import java.util.List;
import java.util.Set;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author dengtao
 * @since 2020/4/29 21:43
 */
public class RemoteResourceFallbackImpl implements FallbackFactory<RemoteResourceService> {
	@Override
	public RemoteResourceService create(Throwable throwable) {
		return new RemoteResourceService() {
			@Override
			public Result<List<ResourceVO>> findResourceByCodes(Set<String> codes) {
				LogUtil.error("调用findResourceByCodes异常：{}", throwable, codes);
				return Result.fail(null, 500);
			}
		};
	}
}
