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

package com.taotao.cloud.member.biz.controller.business.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.boot.common.model.result.PageResult;
import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.member.sys.model.vo.MemberAddressVO;
import com.taotao.cloud.member.biz.model.entity.MemberAddress;
import com.taotao.cloud.member.biz.service.business.IMemberAddressService;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,会员地址API
 *
 * @since 2020-02-25 14:10:16
 */
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/member/manager/member/address")
@Tag(name = "管理端-会员地址管理API", description = "管理端-会员地址管理API")
public class MemberAddressController {

    private final IMemberAddressService memberAddressService;

    @Operation(summary = "会员地址分页列表", description = "会员地址分页列表")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @GetMapping("/{memberId}")
    public Result<PageResult<MemberAddressVO>> queryPage(
            @Validated PageQuery page,
            @Parameter(description = "会员地址ID", required = true) @PathVariable("memberId") Long memberId) {
        IPage<MemberAddress> addressByMember = memberAddressService.queryPage(page, memberId);
        return Result.success(MpUtils.convertMybatisPage(addressByMember, MemberAddressVO.class));
    }

    @Operation(summary = "删除会员收件地址", description = "删除会员收件地址")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @DeleteMapping(value = "/{id}")
    public Result<Boolean> delShippingAddressById(
            @Parameter(description = "会员地址ID", required = true) @PathVariable Long id) {
        return Result.success(memberAddressService.removeMemberAddress(id));
    }

    @Operation(summary = "修改会员收件地址", description = "修改会员收件地址")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @PutMapping
    public Result<Boolean> updateMemberAddress(@Valid MemberAddress shippingAddress) {
        return Result.success(memberAddressService.updateMemberAddress(shippingAddress));
    }

    @Operation(summary = "新增会员收件地址", description = "新增会员收件地址")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @PostMapping
    public Result<Boolean> saveMemberAddress(@Valid MemberAddress shippingAddress) {
        return Result.success(memberAddressService.saveMemberAddress(shippingAddress));
    }
}
