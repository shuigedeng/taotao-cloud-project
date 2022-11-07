/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine Licensed under the Apache License, Version 2.0 (the "License");
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
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.oauth2.core.definition.service;

import com.taotao.cloud.auth.biz.oauth2.core.definition.domain.HerodotusUser;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * <p>Description: 自定义UserDetailsService接口，方便以后扩展 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/1/17 12:49
 */
public interface EnhanceUserDetailsService extends UserDetailsService {

	/**
	 * 通过社交集成的唯一id，获取用户信息
	 * <p>
	 * 如果是短信验证码，openId就是手机号码
	 *
	 * @param accessPrincipal 社交登录提供的相关信息
	 * @param source          社交集成提供商类型
	 * @return {@link UserDetails}
	 * @throws UsernameNotFoundException 用户不存在
	 */
	UserDetails loadUserBySocial(String source, AccessPrincipal accessPrincipal)
		throws AuthenticationException;

	/**
	 * 系统用户名
	 *
	 * @param username 用户账号
	 * @return {@link HerodotusUser}
	 * @throws UsernameNotFoundException 用户不存在
	 */
	HerodotusUser loadHerodotusUserByUsername(String username) throws UsernameNotFoundException;
}
