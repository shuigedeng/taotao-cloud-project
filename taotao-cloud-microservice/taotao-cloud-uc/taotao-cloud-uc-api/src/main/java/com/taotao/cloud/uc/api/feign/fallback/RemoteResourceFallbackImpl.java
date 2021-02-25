package com.taotao.cloud.uc.api.feign.fallback;

import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.model.Result;
import com.taotao.cloud.uc.api.feign.RemoteResourceService;
import com.taotao.cloud.uc.api.vo.resource.ResourceVO;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;
import java.util.Set;

/**
 * RemoteLogFallbackImpl
 *
 * @author dengtao
 * @date 2020/4/29 21:43
 */
public class RemoteResourceFallbackImpl implements FallbackFactory<RemoteResourceService> {
	@Override
	public RemoteResourceService create(Throwable throwable) {
		return new RemoteResourceService() {
			@Override
			public Result<List<ResourceVO>> findResourceByCodes(Set<String> codes) {
				LogUtil.error("调用findResourceByCodes异常：{}", throwable, codes);
				return Result.failed(null, 500);
			}
		};
	}
}
