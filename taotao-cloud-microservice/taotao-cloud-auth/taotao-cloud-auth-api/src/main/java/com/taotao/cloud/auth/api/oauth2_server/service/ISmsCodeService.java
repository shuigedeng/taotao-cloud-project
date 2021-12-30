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
//package com.taotao.cloud.oauth2.api.oauth2_server.service;
//
//import com.taotao.cloud.core.model.Result;
//
///**
// * ISmsCodeService
// *
// * @author shuigedeng
// * @since 2020/4/29 16:27
// * @version 1.0.0
// */
//public interface ISmsCodeService {
//    /**
//     * 保存图形验证码
//     *
//     * @param deviceId  前端唯一标识
//     * @param imageCode 验证码
//     * @return void
//     * @author shuigedeng
//     * @since 2020/4/29 16:27
//     */
//    void saveImageCode(String deviceId, String imageCode);
//
//    /**
//     * 发送验证码
//     *
//     * @param mobile 前端唯一标识(手机号码
//     * @return com.taotao.cloud.common.model.Result<java.lang.String>
//     * @author shuigedeng
//     * @since 2020/4/29 16:27
//     */
//    Result<Boolean> sendSmsCode(String phone);
//
//    /**
//     * 获取验证码
//     *
//     * @param deviceId 前端唯一标识/手机号
//     * @return java.lang.String
//     * @author shuigedeng
//     * @since 2020/4/29 16:28
//     */
//    String getCode(String deviceId);
//
//    /**
//     * 删除验证码
//     *
//     * @param deviceId 前端唯一标识/手机号
//     * @return void
//     * @author shuigedeng
//     * @since 2020/4/29 16:28
//     */
//    void remove(String deviceId);
//
//    /**
//     * 手机验证码校验
//     *
//     * @param phone      手机号码
//     * @param verifyCode 验证码
//     * @return void
//     * @author shuigedeng
//     * @since 2020/4/29 16:28
//     */
//    void validate(String phone, String verifyCode);
//}
