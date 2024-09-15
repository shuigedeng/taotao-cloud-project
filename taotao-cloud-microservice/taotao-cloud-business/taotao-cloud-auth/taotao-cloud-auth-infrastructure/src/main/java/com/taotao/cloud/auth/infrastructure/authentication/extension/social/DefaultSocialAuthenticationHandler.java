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

package com.taotao.cloud.auth.infrastructure.authentication.extension.social;

import com.google.common.collect.ImmutableSet;
import com.taotao.cloud.auth.infrastructure.authentication.extension.social.all.processor.AccessHandlerStrategyFactory;
import com.taotao.cloud.auth.infrastructure.authentication.extension.social.core.definition.AccessUserDetails;
import com.taotao.cloud.auth.infrastructure.authentication.extension.social.core.exception.AccessIdentityVerificationFailedException;
import com.taotao.cloud.auth.infrastructure.authentication.extension.social.handler.AbstractSocialAuthenticationHandler;
import com.taotao.cloud.auth.infrastructure.authentication.extension.social.handler.SocialUserDetails;
import com.taotao.cloud.auth.infrastructure.authentication.userdetails.strategy.local.SysSocialUserService;
import com.taotao.cloud.auth.infrastructure.authentication.userdetails.strategy.local.SysUserService;
import com.taotao.cloud.auth.infrastructure.authentication.userdetails.strategy.user.SysSocialUser;
import com.taotao.cloud.auth.infrastructure.authentication.userdetails.strategy.user.SysUser;
import com.taotao.boot.security.spring.core.AccessPrincipal;
import com.taotao.boot.security.spring.core.userdetails.TtcUser;
import com.taotao.boot.security.spring.exception.SocialCredentialsParameterBindingFailedException;
import com.taotao.boot.security.spring.exception.UsernameAlreadyExistsException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.dromara.hutool.core.bean.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>社交登录默认处理器。 </p>
 *
 * @since : 2022/1/26 23:44
 */
public class DefaultSocialAuthenticationHandler extends AbstractSocialAuthenticationHandler {

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private SysSocialUserService sysSocialUserService;

	@Autowired
	private AccessHandlerStrategyFactory accessHandlerStrategyFactory;

	@Override
	public SocialUserDetails identity(String source, AccessPrincipal accessPrincipal)
		throws AccessIdentityVerificationFailedException {
		AccessUserDetails accessUserDetails =
			accessHandlerStrategyFactory.findAccessUserDetails(source, accessPrincipal);

		if (BeanUtil.isNotEmpty(accessUserDetails)) {
			SysSocialUser sysSocialUser = new SysSocialUser();
			BeanUtil.copyProperties(accessUserDetails, sysSocialUser);
			return sysSocialUser;
		}

		throw new AccessIdentityVerificationFailedException("Access Identity Verification Failed!");
	}

	@Override
	public SocialUserDetails isUserExist(SocialUserDetails socialUserDetails) {
		String uuid = socialUserDetails.getUuid();
		String source = socialUserDetails.getSource();
		if (StringUtils.isNotBlank(uuid) && StringUtils.isNotBlank(source)) {
			return sysSocialUserService.findByUuidAndSource(uuid, source);
		}

		return null;
	}

	@Override
	public TtcUser register(SocialUserDetails socialUserDetails)
		throws UsernameAlreadyExistsException {
		return sysUserService.registerUserDetails(socialUserDetails);
	}

	@Override
	public void binding(String userId, SocialUserDetails socialUserDetails)
		throws SocialCredentialsParameterBindingFailedException {
		if (socialUserDetails instanceof SysSocialUser sysSocialUser) {
			SysUser sysUser = new SysUser();
			sysUser.setUserId(userId);
			sysSocialUser.setUsers(ImmutableSet.<SysUser>builder().add(sysUser).build());
			sysSocialUserService.saveAndFlush(sysSocialUser);
		}
	}

	@Override
	public void additionalRegisterOperation(TtcUser TtcUserDetails,
		SocialUserDetails socialUserDetails) {
	}

	@Override
	public TtcUser signIn(SocialUserDetails socialUserDetails) {
		if (socialUserDetails instanceof SysSocialUser) {
			SysSocialUser sysSocialUser = (SysSocialUser) socialUserDetails;
			SysUser sysUser = sysSocialUser.getUsers().stream().findFirst().orElse(null);
			if (ObjectUtils.isNotEmpty(sysUser)) {
				//				return UpmsHelper.convertSysUserToTtcUser(sysUser);
				return null;
			}
			else {
				return null;
			}
		}

		return null;
	}

	@Override
	public void additionalSignInOperation(
		TtcUser TtcUserDetails,
		SocialUserDetails newSocialUserDetails,
		SocialUserDetails oldSocialUserDetails) {
		if (newSocialUserDetails instanceof SysSocialUser newSysSocialUser
			&& oldSocialUserDetails instanceof SysSocialUser oldSysSocialUser) {
			setSocialUserInfo(
				oldSysSocialUser,
				newSysSocialUser.getAccessToken(),
				newSysSocialUser.getExpireIn(),
				newSysSocialUser.getRefreshToken(),
				newSysSocialUser.getRefreshTokenExpireIn(),
				newSysSocialUser.getScope(),
				newSysSocialUser.getTokenType(),
				newSysSocialUser.getUid(),
				newSysSocialUser.getOpenId(),
				newSysSocialUser.getAccessCode(),
				newSysSocialUser.getUnionId());
			sysSocialUserService.saveAndFlush(oldSysSocialUser);
		}
	}

	protected void setSocialUserInfo(
		SysSocialUser sysSocialUser,
		String accessToken,
		Integer expireIn,
		String refreshToken,
		Integer refreshTokenExpireIn,
		String scope,
		String tokenType,
		String uid,
		String openId,
		String accessCode,
		String unionId) {
		sysSocialUser.setAccessToken(accessToken);
		sysSocialUser.setExpireIn(expireIn);
		sysSocialUser.setRefreshToken(refreshToken);
		sysSocialUser.setRefreshTokenExpireIn(refreshTokenExpireIn);
		sysSocialUser.setScope(scope);
		sysSocialUser.setTokenType(tokenType);
		sysSocialUser.setUid(uid);
		sysSocialUser.setOpenId(openId);
		sysSocialUser.setAccessCode(accessCode);
		sysSocialUser.setUnionId(unionId);
	}
}
