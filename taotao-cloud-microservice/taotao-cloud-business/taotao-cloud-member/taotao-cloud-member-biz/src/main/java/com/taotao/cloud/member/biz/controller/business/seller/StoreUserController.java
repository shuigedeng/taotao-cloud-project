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

package com.taotao.cloud.member.biz.controller.business.seller;

import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.cloud.member.sys.model.vo.MemberVO;
import com.taotao.cloud.member.biz.model.convert.MemberConvert;
import com.taotao.cloud.member.biz.model.entity.Member;
import com.taotao.cloud.member.biz.service.business.IMemberService;
import com.taotao.boot.webmvc.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺端,管理员API
 *
 * @since 2020/11/16 10:57
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "店铺端-管理员API", description = "店铺端-管理员API")
@RequestMapping("/member/seller/store/user")
public class StoreUserController {

    private final IMemberService memberService;

    @Operation(summary = "获取当前登录用户API", description = "获取当前登录用户API")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @GetMapping(value = "/info")
    public Result<MemberVO> getUserInfo() {
        Member member = memberService.findByUsername(SecurityUtils.getUsername());
        member.setPassword(null);
        return Result.success(MemberConvert.INSTANCE.convert(member));
    }
}
