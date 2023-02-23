package com.taotao.cloud.sys.api.feign.fallback;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.sys.api.feign.IFeignMenuApi;
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
public class FeignMenuApiFallback implements FallbackFactory<IFeignMenuApi> {
	@Override
	public IFeignMenuApi create(Throwable throwable) {
		return new IFeignMenuApi() {
			@Override
			public List<MenuQueryVO> findResourceByCodes(Set<String> codes) {
				LogUtils.error("调用findResourceByCodes异常：{}", throwable, codes);
				return null;
			}
		};
	}
}
