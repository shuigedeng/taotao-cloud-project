package com.taotao.cloud.sys.api.feign.fallback;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.sys.api.feign.IFeignRoleApi;
import com.taotao.cloud.sys.api.model.vo.role.RoleQueryVO;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignRoleApiFallback implements FallbackFactory<IFeignRoleApi> {
	@Override
	public IFeignRoleApi create(Throwable throwable) {
		return new IFeignRoleApi() {
			@Override
			public List<RoleQueryVO> findRoleByUserId(Long userId) {
				LogUtils.error("调用findUserInfoByUsername异常：{}", throwable, userId);
				return null;
			}
		};
	}
}
