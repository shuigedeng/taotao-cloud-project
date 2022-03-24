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
//import com.taotao.cloud.core.utils.ResponseUtil;
//import java.io.IOException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
//import org.springframework.stereotype.Component;
//
///**
// * OauthLogoutSuccessHandler
// *
// * @author shuigedeng
// * @since 2020/4/29 21:22
// * @version 2022.03
// */
//@Component
//public class OauthLogoutSuccessHandler implements LogoutSuccessHandler {
//    @Override
//    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
//        ResponseUtil.success(response, "登出成功");
//    }
//}
