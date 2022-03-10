package com.taotao.cloud.sys.api.feign.fallback;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.sys.api.feign.IFeignUserService;
import com.taotao.cloud.sys.api.vo.user.UserQueryVO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignUserFallbackImpl implements FallbackFactory<IFeignUserService> {

	@Override
	public IFeignUserService create(Throwable throwable) {
		return new IFeignUserService() {
			@Override
			public Result<UserQueryVO> findUserInfoByUsername(String username) {
				LogUtil.error("调用findUserInfoByUsername异常：{}", throwable, username);
				return Result.fail(null, 500);
			}

			@Override
			public Result<SecurityUser> getUserInfoBySocial(String providerId, int providerUserId) {
				LogUtil.error("调用getUserInfoBySocial异常：providerId: {}, providerUserId: {}",
					throwable, providerId, providerUserId);
				return Result.fail(null, 500);
			}

			@Override
			public Result<SecurityUser> getSysSecurityUser(
				String nicknameOrUserNameOrPhoneOrEmail) {
				LogUtil.error("调用getUserInfoBySocial异常：nicknameOrUserNameOrPhoneOrEmail: {}",
					nicknameOrUserNameOrPhoneOrEmail);
				return Result.fail(null, 500);
			}
		};
	}
}
