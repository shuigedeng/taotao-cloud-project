package com.taotao.cloud.sys.api.feign.fallback;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.sys.api.feign.IFeignMenuService;
import com.taotao.cloud.sys.api.web.vo.menu.MenuQueryVO;
import java.util.List;
import java.util.Set;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignMenuServiceFallback implements FallbackFactory<IFeignMenuService> {
	@Override
	public IFeignMenuService create(Throwable throwable) {
		return new IFeignMenuService() {
			@Override
			public Result<List<MenuQueryVO>> findResourceByCodes(Set<String> codes) {
				LogUtil.error("调用findResourceByCodes异常：{}", throwable, codes);
				return Result.fail(null, 500);
			}
		};
	}
}
