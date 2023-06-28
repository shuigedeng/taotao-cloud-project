

package com.taotao.cloud.auth.biz.authentication.login.oauth2.social;

import com.google.common.collect.ImmutableSet;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.all.entity.SysSocialUser;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.all.entity.SysUser;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.all.processor.AccessHandlerStrategyFactory;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.all.service.SysSocialUserService;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.all.service.SysUserService;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.core.definition.AccessUserDetails;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.core.exception.AccessIdentityVerificationFailedException;
import com.taotao.cloud.security.springsecurity.core.definition.domain.AccessPrincipal;
import com.taotao.cloud.security.springsecurity.core.definition.domain.HerodotusUser;
import com.taotao.cloud.security.springsecurity.core.definition.domain.SocialUserDetails;
import com.taotao.cloud.security.springsecurity.core.definition.handler.AbstractSocialAuthenticationHandler;
import com.taotao.cloud.security.springsecurity.core.exception.SocialCredentialsParameterBindingFailedException;
import com.taotao.cloud.security.springsecurity.core.exception.UsernameAlreadyExistsException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.dromara.hutool.core.bean.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>Description: 社交登录默认处理器。 </p>
 *
 * 
 * @date : 2022/1/26 23:44
 */
public class DefaultSocialAuthenticationHandler extends AbstractSocialAuthenticationHandler {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysSocialUserService sysSocialUserService;
    @Autowired
    private AccessHandlerStrategyFactory accessHandlerStrategyFactory;

	@Override
	public SocialUserDetails identity(String source, AccessPrincipal accessPrincipal) throws AccessIdentityVerificationFailedException {
		AccessUserDetails accessUserDetails = accessHandlerStrategyFactory.findAccessUserDetails(source, accessPrincipal);

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
	public HerodotusUser register(SocialUserDetails socialUserDetails) throws UsernameAlreadyExistsException {
		return sysUserService.registerUserDetails(socialUserDetails);
	}

	@Override
	public void binding(String userId, SocialUserDetails socialUserDetails) throws SocialCredentialsParameterBindingFailedException {
		if (socialUserDetails instanceof SysSocialUser sysSocialUser) {
			SysUser sysUser = new SysUser();
			sysUser.setUserId(userId);
			sysSocialUser.setUsers(ImmutableSet.<SysUser>builder().add(sysUser).build());
			sysSocialUserService.saveAndFlush(sysSocialUser);
		}
	}

	@Override
	public void additionalRegisterOperation(HerodotusUser herodotusUserDetails, SocialUserDetails socialUserDetails) {

	}

	@Override
	public HerodotusUser signIn(SocialUserDetails socialUserDetails) {
		if (socialUserDetails instanceof SysSocialUser) {
			SysSocialUser sysSocialUser = (SysSocialUser) socialUserDetails;
			SysUser sysUser = sysSocialUser.getUsers().stream().findFirst().orElse(null);
			if (ObjectUtils.isNotEmpty(sysUser)) {
//				return UpmsHelper.convertSysUserToHerodotusUser(sysUser);
				return null;
			} else {
				return null;
			}
		}

		return null;
	}

	@Override
	public void additionalSignInOperation(HerodotusUser herodotusUserDetails, SocialUserDetails newSocialUserDetails, SocialUserDetails oldSocialUserDetails) {
		if (newSocialUserDetails instanceof SysSocialUser newSysSocialUser && oldSocialUserDetails instanceof SysSocialUser oldSysSocialUser) {
			setSocialUserInfo(oldSysSocialUser, newSysSocialUser.getAccessToken(), newSysSocialUser.getExpireIn(), newSysSocialUser.getRefreshToken(), newSysSocialUser.getRefreshTokenExpireIn(), newSysSocialUser.getScope(), newSysSocialUser.getTokenType(), newSysSocialUser.getUid(), newSysSocialUser.getOpenId(), newSysSocialUser.getAccessCode(), newSysSocialUser.getUnionId());
			sysSocialUserService.saveAndFlush(oldSysSocialUser);
		}
	}

	protected void setSocialUserInfo(SysSocialUser sysSocialUser, String accessToken, Integer expireIn, String refreshToken, Integer refreshTokenExpireIn, String scope, String tokenType, String uid, String openId, String accessCode, String unionId) {
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