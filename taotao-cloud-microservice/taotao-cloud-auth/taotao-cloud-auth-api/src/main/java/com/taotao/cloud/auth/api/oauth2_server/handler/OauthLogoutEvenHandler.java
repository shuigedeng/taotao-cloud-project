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
//import cn.hutool.core.collection.CollUtil;
//import com.taotao.cloud.common.utils.LogUtil;
//import com.taotao.cloud.core.model.SecurityUser;
//import org.springframework.context.ApplicationListener;
//import org.springframework.security.authentication.event.LogoutSuccessEvent;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Component;
//
///**
// * 在验证过程中发生异常会触发此类事件
// *
// * @author shuigedeng
// * @since 2020/4/29 21:23
// * @version 1.0.0
// */
//@Component
//public class OauthLogoutEvenHandler implements ApplicationListener<LogoutSuccessEvent> {
//    @Override
//    public void onApplicationEvent(LogoutSuccessEvent event) {
//        Authentication authentication = event.getAuthentication();
//        if (CollUtil.isNotEmpty(authentication.getAuthorities())) {
//            Object principal = authentication.getPrincipal();
//            if (principal instanceof SecurityUser) {
//                // 此处可以异步调用消息系统 发送消息或者邮件
//                LogUtil.info("用户：{0} 登出成功", ((SecurityUser) principal).getUsername());
//            }
//        }
//    }
//}
