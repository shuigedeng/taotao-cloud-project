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

package com.taotao.cloud.auth.biz.authentication.login.oauth2.social.wxapp.processor;

import static com.taotao.cloud.common.constant.SymbolConstants.FORWARD_SLASH;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.all.enums.AccountType;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.core.definition.AccessHandler;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.core.definition.AccessResponse;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.core.definition.AccessUserDetails;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.core.exception.AccessIdentityVerificationFailedException;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.core.exception.AccessPreProcessFailedException;
import com.taotao.cloud.security.springsecurity.core.domain.AccessPrincipal;
import org.apache.commons.lang3.ObjectUtils;

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
        WxMaUserInfo wxMaUserInfo = wxappProcessor.getUserInfo(
                accessPrincipal.getAppId(),
                accessPrincipal.getSessionKey(),
                accessPrincipal.getEncryptedData(),
                accessPrincipal.getIv());
        if (ObjectUtils.isNotEmpty(wxMaUserInfo)) {
            return convertWxMaUserInfoToAccessUserDetails(wxMaUserInfo, accessPrincipal);
        }

        throw new AccessIdentityVerificationFailedException("Can not find the userinfo from Wechat!");
    }

    private AccessUserDetails convertWxMaUserInfoToAccessUserDetails(
            WxMaUserInfo wxMaUserInfo, AccessPrincipal accessPrincipal) {
        AccessUserDetails accessUserDetails = new AccessUserDetails();
        accessUserDetails.setUuid(accessPrincipal.getOpenId());
        accessUserDetails.setUserName(wxMaUserInfo.getNickName());
        accessUserDetails.setNickName(wxMaUserInfo.getNickName());
        accessUserDetails.setAvatar(wxMaUserInfo.getAvatarUrl());
        accessUserDetails.setLocation(wxMaUserInfo.getCountry()
                + FORWARD_SLASH
                + wxMaUserInfo.getProvince()
                + FORWARD_SLASH
                + wxMaUserInfo.getCity());
        accessUserDetails.setSource(AccountType.WXAPP.name());
        accessUserDetails.setOpenId(accessPrincipal.getOpenId());
        accessUserDetails.setUnionId(accessPrincipal.getUnionId());
        accessUserDetails.setAppId(wxMaUserInfo.getWatermark().getAppid());
        accessUserDetails.setPhoneNumber(wxMaUserInfo.getWatermark().getAppid());
        return accessUserDetails;
    }
}
