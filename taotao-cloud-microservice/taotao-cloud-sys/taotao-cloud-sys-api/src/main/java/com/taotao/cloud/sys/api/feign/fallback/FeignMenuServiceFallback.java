package com.taotao.cloud.sys.api.feign.fallback;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.sys.api.feign.IFeignMenuService;
import com.taotao.cloud.sys.api.model.vo.menu.MenuQueryVO;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;
import java.util.Set;

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
			public List<MenuQueryVO> findResourceByCodes(Set<String> codes) {
				LogUtils.error("调用findResourceByCodes异常：{}", throwable, codes);
				return null;
			}
		};
	}
}
