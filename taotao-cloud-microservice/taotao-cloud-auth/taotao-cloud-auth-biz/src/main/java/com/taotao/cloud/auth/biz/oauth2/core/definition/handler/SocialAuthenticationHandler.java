/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
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

package com.taotao.cloud.auth.biz.oauth2.core.definition.handler;

import com.taotao.cloud.auth.biz.oauth2.core.definition.domain.HerodotusUser;
import org.springframework.security.core.AuthenticationException;

/**
 * <p>Description: 社交登录处理器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/4/4 17:34
 */
public interface SocialAuthenticationHandler {

	/**
	 * 社交登录
	 * <p>
	 * 1. 首先在第三方系统进行认证，或者手机号码、扫码认证。返回认证后的信息 2. 根据认证返回的信息，在系统中查询是否有对应的用户信息。 2.1.
	 * 如果有对应的信息，根据需要更新社交用户的信息，然后返回系统用户信息，进行登录。 2.2. 如果没有对应信息，就先进行用户的注册，然后进行社交用户和系统用户的绑定。
	 *
	 * @param source          社交登录提供者分类
	 * @param accessPrincipal 社交登录所需要的信息 {@link AccessPrincipal}
	 * @return 系统用户
	 * @throws AuthenticationException {@link AuthenticationException} 认证错误
	 */
	HerodotusUser authentication(String source, AccessPrincipal accessPrincipal)
		throws AuthenticationException;

}
