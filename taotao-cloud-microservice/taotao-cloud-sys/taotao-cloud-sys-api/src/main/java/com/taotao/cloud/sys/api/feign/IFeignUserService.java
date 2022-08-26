package com.taotao.cloud.sys.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.sys.api.feign.fallback.FeignUserServiceFallback;
import com.taotao.cloud.sys.api.web.vo.user.UserQueryVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用后台用户模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(name = ServiceName.TAOTAO_CLOUD_SYS, fallbackFactory = FeignUserServiceFallback.class)
public interface IFeignUserService {

	/**
	 * 获取用户信息
	 *
	 * @param username 用户名称
	 * @return 用户信息
	 * @since 2020/10/21 15:06
	 */
	@GetMapping(value = "/user/info/username")
	Result<UserQueryVO> findUserInfoByUsername(@RequestParam(value = "username") String username);

	/**
	 * 通过第三方查询用户包括角色权限等
	 *
	 * @param providerId     providerId
	 * @param providerUserId providerUserId
	 * @return 系统用户信息
	 * @since 2020/4/29 17:47
	 */
	@GetMapping(value = "/user/info/social/{social}", headers = {"from=in"})
	Result<SecurityUser> getUserInfoBySocial(@RequestParam("providerId") String providerId,
		@RequestParam("providerUserId") int providerUserId);

	/**
	 * 通过用户名查询用户包括角色权限等
	 *
	 * @param nicknameOrUserNameOrPhoneOrEmail 用户名
	 * @return 系统用户信息
	 * @since 2020/4/29 17:48
	 */
	@GetMapping(value = "/sys/info/security")
	Result<SecurityUser> getSysSecurityUser(String nicknameOrUserNameOrPhoneOrEmail);

}

