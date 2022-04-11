package com.taotao.cloud.sys.api.feign.fallback;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.sys.api.feign.IFeignSettingService;
import com.taotao.cloud.sys.api.vo.setting.GoodsSettingVO;
import com.taotao.cloud.sys.api.vo.setting.SettingVO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * FeignSettingFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignSettingFallback implements FallbackFactory<IFeignSettingService> {

	@Override
	public IFeignSettingService create(Throwable throwable) {
		return new IFeignSettingService() {
			@Override
			public Result<SettingVO> get(String key) {
				return null;
			}

			@Override
			public Result<GoodsSettingVO> getGoodsSetting(
				String name) {
				return null;
			}
		};
	}
}
