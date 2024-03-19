///*
// * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
//
//package com.taotao.cloud.sys.biz.controller.feign;
//
//import com.taotao.cloud.common.model.BaseSecurityUser;
//import com.taotao.cloud.sys.api.feign.IFeignUserApi;
//import com.taotao.cloud.sys.biz.model.vo.user.UserQueryVO;
//import com.taotao.cloud.sys.biz.model.entity.system.User;
//import com.taotao.cloud.sys.biz.service.business.IUserService;
//import com.taotao.cloud.sys.biz.service.feign.IFeignUserService;
//import com.taotao.cloud.web.base.controller.BaseFeignController;
//import lombok.AllArgsConstructor;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * 内部服务端-字典API
// *
// * @author shuigedeng
// * @version 2021.9
// * @since 2021-10-09 14:24:19
// */
//@Validated
//@RestController
//@AllArgsConstructor
//public class FeignUserController extends BaseFeignController<IUserService, User, Long> implements IFeignUserApi {
//
//    private final IFeignUserService feignUserService;
//
//    @Override
//    public UserQueryVO findUserInfoByUsername(String username) {
//        return null;
//    }
//
//    @Override
//    public BaseSecurityUser getUserInfoBySocial(String providerId, int providerUserId) {
//        return null;
//    }
//
//    @Override
//    public BaseSecurityUser getSysSecurityUser(String nicknameOrUserNameOrPhoneOrEmail) {
//        return null;
//    }
//}
