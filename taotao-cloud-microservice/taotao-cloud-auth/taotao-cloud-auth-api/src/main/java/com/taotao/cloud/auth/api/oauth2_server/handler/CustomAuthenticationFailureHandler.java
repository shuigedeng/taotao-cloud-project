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
//import com.taotao.cloud.common.enums.ResultEnum;
//import com.taotao.cloud.core.utils.ResponseUtil;
//import com.taotao.cloud.security.exception.ValidateCodeException;
//import java.io.IOException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.stereotype.Component;
//
///**
// * 认证失败处理器
// *
// * @author shuigedeng
// * @since 2020/5/13 16:08
// * @version 2022.03
// */
//@Component
//public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
//    @Override
//    public void onAuthenticationFailure(HttpServletRequest request,
//                                        HttpServletResponse response,
//                                        AuthenticationException exception) throws IOException {
//        String message;
//        if (exception instanceof ValidateCodeException) {
//            message = exception.getMessage();
//        } else if (exception instanceof BadCredentialsException) {
//            message = "用户名或者密码错误";
//        } else {
//            message = "用户认证失败";
//        }
//        ResponseUtil.failed(response, ResultEnum.ERROR.getCode(), message);
//    }
//}
//
//
