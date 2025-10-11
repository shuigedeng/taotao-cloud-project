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

package com.taotao.cloud.store.biz.api.controller.seller;

import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.security.spring.model.SecurityUser;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.cloud.member.api.feign.MemberApi;
import com.taotao.cloud.member.api.model.vo.MemberVO;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 店铺端,管理员接口 */
@Validated
@RestController
@Tag(name = "店铺端-管理员接口", description = "店铺端-管理员接口")
@RequestMapping("/store/user")
public class StoreUserController {

    @Autowired
    private MemberApi memberApi;

    @Operation(summary = "获取当前登录用户接口", description = "获取当前登录用户接口")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/info")
    public Result<MemberVO> getUserInfo() {
        SecurityUser tokenUser = SecurityUtils.getCurrentUser();
        if (tokenUser != null) {
            MemberVO member = memberApi.findByUsername(tokenUser.getUsername());
            // member.setPassword(null);
            return Result.success(member);
        }
        throw new BusinessException(ResultEnum.USER_NOT_LOGIN);
    }
}
