///*
// * Copyright 2002-2021 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.taotao.cloud.oauth2.api.oauth2_server.handler;
//
//import cn.hutool.core.util.StrUtil;
//import com.taotao.cloud.common.utils.LogUtil;
//import com.taotao.cloud.core.utils.AuthUtil;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.common.OAuth2RefreshToken;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.web.authentication.logout.LogoutHandler;
//import org.springframework.stereotype.Component;
//import org.springframework.util.Assert;
//
///**
// * OauthLogoutHandler
// *
// * @author shuigedeng
// * @since 2020/4/29 21:21
// * @version 2022.03
// */
//@Component
//public class OauthLogoutHandler implements LogoutHandler {
//    @Autowired
//    private TokenStore tokenStore;
//
//    @Override
//    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//        Assert.notNull(tokenStore, "tokenStore must be set");
//        String token = request.getParameter("token");
//        if (StrUtil.isEmpty(token)) {
//            token = AuthUtil.extractToken(request);
//        }
//
//        if (StrUtil.isNotEmpty(token)) {
//            OAuth2AccessToken existingAccessToken = tokenStore.readAccessToken(token);
//            OAuth2RefreshToken refreshToken;
//            if (existingAccessToken != null) {
//                if (existingAccessToken.getRefreshToken() != null) {
//                    LogUtil.info("remove refreshToken! {0}", existingAccessToken.getRefreshToken());
//                    refreshToken = existingAccessToken.getRefreshToken();
//                    tokenStore.removeRefreshToken(refreshToken);
//                }
//                LogUtil.info("remove existingAccessToken! {0}", existingAccessToken);
//                tokenStore.removeAccessToken(existingAccessToken);
//            }
//        }
//    }
//}
