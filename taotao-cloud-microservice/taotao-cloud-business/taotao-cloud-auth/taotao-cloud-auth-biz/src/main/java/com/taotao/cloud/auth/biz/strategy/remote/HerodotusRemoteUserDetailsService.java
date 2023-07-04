/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Cloud licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Cloud 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/dromara/dante-cloud
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/dromara/dante-cloud
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.strategy.remote;

import com.taotao.cloud.auth.biz.strategy.AbstractStrategyUserDetailsService;
import com.taotao.cloud.security.springsecurity.core.domain.AccessPrincipal;
import com.taotao.cloud.security.springsecurity.core.domain.HerodotusGrantedAuthority;
import com.taotao.cloud.security.springsecurity.core.domain.HerodotusUser;
import com.taotao.cloud.sys.api.feign.IFeignUserApi;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>Description: UserDetail远程调用服务 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/5/23 9:21
 */
public class HerodotusRemoteUserDetailsService extends AbstractStrategyUserDetailsService {

	private final IFeignUserApi userApi;

	public HerodotusRemoteUserDetailsService(IFeignUserApi userApi) {
		this.userApi = userApi;
	}

	@Override
	public HerodotusUser findUserDetailsByUsername(String userName) throws UsernameNotFoundException {
//		Result<SysUser> result = remoteUserDetailsService.findByUserName(userName);
//
//		SysUser sysUser = result.getData();
//		return this.convertSysUser(sysUser, userName);
//		return new HerodotusUser();

		Collection<HerodotusGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new HerodotusGrantedAuthority("manager.book.read"));
		authorities.add(new HerodotusGrantedAuthority("manager.book.write"));
		Set<String> roles = new HashSet<>();
		roles.add("ROLE_A1");
		roles.add("ROLE_A2");
		// admin/123456
		HerodotusUser user = new HerodotusUser("33e781c5-31e0-4ea4-8b02-1236bde9643", "admin",
			"{bcrypt}$2a$10$lvjys/FAHAVmgXM.U1LtOOJ./C5SstExZCZ0Z5N7SeGZAue0JFtXC",
			true, true, true, true,
			authorities, roles, "", "");
		return user;
	}

	@Override
	public HerodotusUser findUserDetailsBySocial(String source, AccessPrincipal accessPrincipal) {
//		Result<HerodotusUser> result = remoteSocialDetailsService.findUserDetailsBySocial(source, accessPrincipal);
//		return result.getData();
		return new HerodotusUser();
	}
}
