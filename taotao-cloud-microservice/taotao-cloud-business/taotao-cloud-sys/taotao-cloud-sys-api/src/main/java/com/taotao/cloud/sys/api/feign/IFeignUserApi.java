package com.taotao.cloud.sys.api.feign;

import static com.taotao.cloud.openfeign.api.VersionEnum.V2022_07;
import static com.taotao.cloud.openfeign.api.VersionEnum.V2022_08;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.openfeign.api.ApiInfo;
import com.taotao.cloud.openfeign.api.ApiInfo.Create;
import com.taotao.cloud.openfeign.api.ApiInfo.Update;
import com.taotao.cloud.sys.api.feign.fallback.FeignUserApiFallback;
import com.taotao.cloud.sys.api.model.vo.user.UserQueryVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用后台用户模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(name = ServiceName.TAOTAO_CLOUD_SYS, contextId = "feignUserApi", fallbackFactory = FeignUserApiFallback.class)
public interface IFeignUserApi {

	/**
	 * 获取用户信息
	 *
	 * @param username 用户名称
	 * @return 用户信息
	 * @since 2020/10/21 15:06
	 */
	@ApiInfo(
		create = @Create(version = V2022_07, date = "2022-07-01 17:11:55"),
		update = {
			@Update(version = V2022_07, content = "主要修改了配置信息的接口查询", date = "2022-07-01 17:11:55"),
			@Update(version = V2022_08, content = "主要修改了配置信息的接口查询08", date = "2022-07-01 17:11:55")
		}
	)
	@GetMapping(value = "/sys/feign/user/info/username")
	UserQueryVO findUserInfoByUsername(@RequestParam(value = "username") String username);

	/**
	 * 通过第三方查询用户包括角色权限等
	 *
	 * @param providerId     providerId
	 * @param providerUserId providerUserId
	 * @return 系统用户信息
	 * @since 2020/4/29 17:47
	 */
	@ApiInfo(
		create = @Create(version = V2022_07, date = "2022-07-01 17:11:55"),
		update = {
			@Update(version = V2022_07, content = "主要修改了配置信息的接口查询", date = "2022-07-01 17:11:55"),
			@Update(version = V2022_08, content = "主要修改了配置信息的接口查询08", date = "2022-07-01 17:11:55")
		}
	)
	@GetMapping(value = "/sys/feign/user/info/social/{social}", headers = {"from=in"})
	SecurityUser getUserInfoBySocial(@RequestParam("providerId") String providerId,
		@RequestParam("providerUserId") int providerUserId);

	/**
	 * 通过用户名查询用户包括角色权限等
	 *
	 * @param nicknameOrUserNameOrPhoneOrEmail 用户名
	 * @return 系统用户信息
	 * @since 2020/4/29 17:48
	 */
	@ApiInfo(
		create = @Create(version = V2022_07, date = "2022-07-01 17:11:55"),
		update = {
			@Update(version = V2022_07, content = "主要修改了配置信息的接口查询", date = "2022-07-01 17:11:55"),
			@Update(version = V2022_08, content = "主要修改了配置信息的接口查询08", date = "2022-07-01 17:11:55")
		}
	)
	@GetMapping(value = "/sys/feign/info/security")
	SecurityUser getSysSecurityUser(String nicknameOrUserNameOrPhoneOrEmail);

}

