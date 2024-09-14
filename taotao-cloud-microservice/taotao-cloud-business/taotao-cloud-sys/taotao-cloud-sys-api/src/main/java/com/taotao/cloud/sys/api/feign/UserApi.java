/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.sys.api.feign;


import com.taotao.boot.common.constant.ServiceName;
import com.taotao.boot.common.model.BaseSecurityUser;
import com.taotao.boot.common.support.info.ApiInfo;
import com.taotao.boot.common.support.info.Create;
import com.taotao.boot.common.support.info.Update;
import com.taotao.cloud.sys.api.feign.fallback.UserApiFallback;
import com.taotao.cloud.sys.api.feign.response.UserQueryApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.taotao.boot.common.support.info.ApiVersionEnum.V2022_07;
import static com.taotao.boot.common.support.info.ApiVersionEnum.V2022_08;

/**
 * 远程调用后台用户模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(
	name = ServiceName.TAOTAO_CLOUD_SYS,
	contextId = "IFeignUserApi",
	fallbackFactory = UserApiFallback.class)
public interface UserApi {

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
		})
	@GetMapping(value = "/sys/feign/user/info/username")
	UserQueryApiResponse findUserInfoByUsername(@RequestParam(value = "username") String username);

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
		})
	@GetMapping(
		value = "/sys/feign/user/info/social/{social}",
		headers = {"from=in"})
	BaseSecurityUser getUserInfoBySocial(
		@RequestParam("providerId") String providerId, @RequestParam("providerUserId") int providerUserId);

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
		})
	@GetMapping(value = "/sys/feign/info/security")
	BaseSecurityUser getSysSecurityUser(String nicknameOrUserNameOrPhoneOrEmail);
}
