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

package com.taotao.cloud.auth.biz.authentication.login.oauth2.social.justauth.processor;

import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.core.definition.AccessHandler;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.core.definition.AccessResponse;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.core.definition.AccessUserDetails;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.core.exception.AccessIdentityVerificationFailedException;
import com.taotao.cloud.security.springsecurity.core.definition.domain.AccessPrincipal;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import org.apache.commons.lang3.ObjectUtils;

/**
 * <p>Description: JustAuth 接入处理器 </p>
 *
 * 
 * @date : 2022/1/25 17:45
 */
public class JustAuthAccessHandler implements AccessHandler {

    private final JustAuthProcessor justAuthProcessor;

    public JustAuthAccessHandler(JustAuthProcessor justAuthProcessor) {
        this.justAuthProcessor = justAuthProcessor;
    }

    @Override
    public AccessResponse preProcess(String core, String... params) {
        String url = justAuthProcessor.getAuthorizeUrl(core);

        AccessResponse accessResponse = new AccessResponse();
        accessResponse.setAuthorizeUrl(url);
        return accessResponse;
    }

    @Override
    public AccessUserDetails loadUserDetails(String source, AccessPrincipal accessPrincipal) {
        AuthRequest authRequest = justAuthProcessor.getAuthRequest(source);

        AuthCallback authCallback = AuthCallback.builder()
                .code(accessPrincipal.getCode())
                .auth_code(accessPrincipal.getAuth_code())
                .state(accessPrincipal.getState())
                .authorization_code(accessPrincipal.getAuthorization_code())
                .oauth_token(accessPrincipal.getOauth_token())
                .oauth_verifier(accessPrincipal.getOauth_verifier())
                .build();

        AuthResponse<AuthUser> response = authRequest.login(authCallback);
        if (response.ok()) {
            AuthUser authUser = response.getData();
            return convertAuthUserToAccessUserDetails(authUser);
        }

        throw new AccessIdentityVerificationFailedException(response.getMsg());
    }

    private AccessUserDetails convertAuthUserToAccessUserDetails(AuthUser authUser) {
        AccessUserDetails sysSocialUser = new AccessUserDetails();
        sysSocialUser.setUuid(authUser.getUuid());
        sysSocialUser.setUserName(authUser.getUsername());
        sysSocialUser.setNickName(authUser.getNickname());
        sysSocialUser.setAvatar(authUser.getAvatar());
        sysSocialUser.setBlog(authUser.getBlog());
        sysSocialUser.setCompany(authUser.getCompany());
        sysSocialUser.setLocation(authUser.getLocation());
        sysSocialUser.setEmail(authUser.getEmail());
        sysSocialUser.setRemark(authUser.getRemark());
        sysSocialUser.setGender(authUser.getGender());
        sysSocialUser.setSource(authUser.getSource());
        AuthToken authToken = authUser.getToken();
        if (ObjectUtils.isNotEmpty(authToken)) {
            setAccessUserInfo(sysSocialUser, authToken.getAccessToken(), authToken.getExpireIn(), authToken.getRefreshToken(), authToken.getRefreshTokenExpireIn(), authToken.getScope(), authToken.getTokenType(), authToken.getUid(), authToken.getOpenId(), authToken.getAccessCode(), authToken.getUnionId());
        }

        return sysSocialUser;
    }

    private void setAccessUserInfo(AccessUserDetails accessUserDetails, String accessToken, Integer expireIn, String refreshToken, Integer refreshTokenExpireIn, String scope, String tokenType, String uid, String openId, String accessCode, String unionId) {
        accessUserDetails.setAccessToken(accessToken);
        accessUserDetails.setExpireIn(expireIn);
        accessUserDetails.setRefreshToken(refreshToken);
        accessUserDetails.setRefreshTokenExpireIn(refreshTokenExpireIn);
        accessUserDetails.setScope(scope);
        accessUserDetails.setTokenType(tokenType);
        accessUserDetails.setUid(uid);
        accessUserDetails.setOpenId(openId);
        accessUserDetails.setAccessCode(accessCode);
        accessUserDetails.setUnionId(unionId);
    }
}
