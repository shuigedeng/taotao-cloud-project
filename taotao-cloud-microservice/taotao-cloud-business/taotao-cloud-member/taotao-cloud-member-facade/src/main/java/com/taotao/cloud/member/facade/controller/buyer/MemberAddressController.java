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

package com.taotao.cloud.member.facade.controller.buyer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.PageQuery;
import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.data.mybatis.mybatisplus.utils.MpUtils;
import com.taotao.cloud.member.application.command.address.dto.clientobject.MemberAddressCO;
import com.taotao.cloud.member.application.converter.MemberAddressConvert;
import com.taotao.cloud.member.application.service.IMemberAddressService;
import com.taotao.cloud.member.infrastructure.persistent.po.MemberAddress;
import com.taotao.cloud.security.springsecurity.utils.SecurityUtils;
import com.taotao.cloud.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端-会员地址API
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:54:53
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "买家端-会员地址API", description = "买家端-会员地址API")
@RequestMapping("/member/buyer/member/address")
public class MemberAddressController {

	private final IMemberAddressService memberAddressService;

	@Operation(summary = "分页获取当前会员收件地址列表", description = "分页获取当前会员收件地址列表")
	@RequestLogger
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping
	public Result<PageResult<MemberAddressCO>> page(@Validated PageQuery page) {
		IPage<MemberAddress> memberAddressPage = memberAddressService.queryPage(page,
			SecurityUtils.getUserId());
		return Result.success(
			MpUtils.convertMybatisPage(memberAddressPage, MemberAddressConvert.INSTANCE::convert));
	}

	@Operation(summary = "根据ID获取会员收件地址", description = "根据ID获取会员收件地址")
	@RequestLogger
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/{id}")
	public Result<MemberAddressCO> getShippingAddress(
		@Parameter(description = "会员地址ID", required = true)
		@NotNull(message = "id不能为空")
		@PathVariable(value = "id") Long id) {
		MemberAddress memberAddress = memberAddressService.getMemberAddress(id);
		return Result.success(MemberAddressConvert.INSTANCE.convert(memberAddress));
	}

	@Operation(summary = "获取当前会员默认收件地址", description = "获取当前会员默认收件地址")
	@RequestLogger
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/current/default")
	public Result<MemberAddressCO> getDefaultShippingAddress() {
		MemberAddress memberAddress = memberAddressService.getDefaultMemberAddress();
		return Result.success(MemberAddressConvert.INSTANCE.convert(memberAddress));
	}

	@Operation(summary = "新增会员收件地址", description = "新增会员收件地址")
	@RequestLogger
	@PreAuthorize("@el.check('admin','timing:list')")
	@PostMapping
	public Result<Boolean> addShippingAddress(@Valid MemberAddress shippingAddress) {
		// 添加会员地址
		shippingAddress.setMemberId(SecurityUtils.getUserId());
		if (shippingAddress.getDefaulted() == null) {
			shippingAddress.setDefaulted(false);
		}
		return Result.success(memberAddressService.saveMemberAddress(shippingAddress));
	}

	@Operation(summary = "修改会员收件地址", description = "修改会员收件地址")
	@RequestLogger
	@PreAuthorize("@el.check('admin','timing:list')")
	@PutMapping
	public Result<Boolean> editShippingAddress(@Valid @RequestBody MemberAddress shippingAddress) {
		return Result.success(memberAddressService.updateMemberAddress(shippingAddress));
	}

	@Operation(summary = "删除会员收件地址", description = "删除会员收件地址")
	@RequestLogger
	@PreAuthorize("@el.check('admin','timing:list')")
	@DeleteMapping(value = "/{id}")
	public Result<Boolean> delShippingAddressById(
		@Parameter(description = "会员地址ID", required = true)
		@NotNull(message = "id不能为空")
		@PathVariable(value = "id")
		Long id) {
		return Result.success(memberAddressService.removeMemberAddress(id));
	}
}
