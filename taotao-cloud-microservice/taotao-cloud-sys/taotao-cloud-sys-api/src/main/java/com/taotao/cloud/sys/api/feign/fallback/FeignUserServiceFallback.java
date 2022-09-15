package com.taotao.cloud.sys.api.feign.fallback;

import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.sys.api.feign.IFeignUserService;
import com.taotao.cloud.sys.api.model.vo.user.UserQueryVO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignUserServiceFallback implements FallbackFactory<IFeignUserService> {
	@Override
	public IFeignUserService create(Throwable throwable) {
		return new IFeignUserService() {
			@Override
			public UserQueryVO findUserInfoByUsername(String username) {
				LogUtils.error("调用findUserInfoByUsername异常：{}", throwable, username);
				return null;
			}

			@Override
			public SecurityUser getUserInfoBySocial(String providerId, int providerUserId) {
				LogUtils.error("调用getUserInfoBySocial异常：providerId: {}, providerUserId: {}",
					throwable, providerId, providerUserId);
				return null;
			}

			@Override
			public SecurityUser getSysSecurityUser(
				String nicknameOrUserNameOrPhoneOrEmail) {
				LogUtils.error("调用getUserInfoBySocial异常：nicknameOrUserNameOrPhoneOrEmail: {}",
					nicknameOrUserNameOrPhoneOrEmail);
				return null;
			}
		};
	}
}
