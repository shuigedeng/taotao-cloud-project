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

package com.taotao.cloud.auth.biz.authentication.login.oauth2.social.wxapp.processor;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.all.enums.AccountType;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.core.definition.AccessHandler;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.core.definition.AccessResponse;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.core.definition.AccessUserDetails;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.core.exception.AccessIdentityVerificationFailedException;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.core.exception.AccessPreProcessFailedException;
import com.taotao.cloud.security.springsecurity.core.definition.domain.AccessPrincipal;
import org.apache.commons.lang3.ObjectUtils;

import static com.taotao.cloud.common.constant.SymbolConstants.FORWARD_SLASH;

/**
 * <p>Description: 微信小程序接入处理器 </p>
 *
 * 
 * @date : 2022/1/26 10:56
 */
public class WxappAccessHandler implements AccessHandler {

	private final WxappProcessor wxappProcessor;

	public WxappAccessHandler(WxappProcessor wxappProcessor) {
		this.wxappProcessor = wxappProcessor;
	}

	@Override
	public AccessResponse preProcess(String core, String... params) {
		WxMaJscode2SessionResult wxMaSession = wxappProcessor.login(core, params[0]);
		if (ObjectUtils.isNotEmpty(wxMaSession)) {
			AccessResponse accessResponse = new AccessResponse();
			accessResponse.setSession(wxMaSession);
			return accessResponse;
		}

		throw new AccessPreProcessFailedException("Wxapp login failed");
	}

	@Override
	public AccessUserDetails loadUserDetails(String source, AccessPrincipal accessPrincipal) {
		WxMaUserInfo wxMaUserInfo = wxappProcessor.getUserInfo(accessPrincipal.getAppId(), accessPrincipal.getSessionKey(), accessPrincipal.getEncryptedData(), accessPrincipal.getIv());
		if (ObjectUtils.isNotEmpty(wxMaUserInfo)) {
			return convertWxMaUserInfoToAccessUserDetails(wxMaUserInfo, accessPrincipal);
		}

		throw new AccessIdentityVerificationFailedException("Can not find the userinfo from Wechat!");
	}

	private AccessUserDetails convertWxMaUserInfoToAccessUserDetails(WxMaUserInfo wxMaUserInfo, AccessPrincipal accessPrincipal) {
		AccessUserDetails accessUserDetails = new AccessUserDetails();
		accessUserDetails.setUuid(accessPrincipal.getOpenId());
		accessUserDetails.setUserName(wxMaUserInfo.getNickName());
		accessUserDetails.setNickName(wxMaUserInfo.getNickName());
		accessUserDetails.setAvatar(wxMaUserInfo.getAvatarUrl());
		accessUserDetails.setLocation(wxMaUserInfo.getCountry() + FORWARD_SLASH + wxMaUserInfo.getProvince() + FORWARD_SLASH + wxMaUserInfo.getCity());
		accessUserDetails.setSource(AccountType.WXAPP.name());
		accessUserDetails.setOpenId(accessPrincipal.getOpenId());
		accessUserDetails.setUnionId(accessPrincipal.getUnionId());
		accessUserDetails.setAppId(wxMaUserInfo.getWatermark().getAppid());
		accessUserDetails.setPhoneNumber(wxMaUserInfo.getWatermark().getAppid());
		return accessUserDetails;
	}
}
