/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.aftersale.biz.controller;

import com.taotao.cloud.aftersale.api.vo.WithdrawVO;
import com.taotao.cloud.aftersale.biz.entity.Withdraw;
import com.taotao.cloud.aftersale.biz.mapper.WithdrawMapper;
import com.taotao.cloud.aftersale.biz.service.IWithdrawService;
import com.taotao.cloud.core.model.Result;
import com.taotao.cloud.log.annotation.RequestOperateLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提现申请管理API
 *
 * @author dengtao
 * @date 2020/11/13 09:58
 * @since v1.0
 */
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/withdraw")
@Api(value = "提现申请管理API", tags = {"提现申请管理API"})
public class WithdrawController {

	private final IWithdrawService withdrawService;

	@ApiOperation("根据id查询提现申请信息")
	@RequestOperateLog(description = "根据id查询提现申请信息")
	@PreAuthorize("hasAuthority('withdraw:info:id')")
	@GetMapping("/info/id/{id:[0-9]*}")
	public Result<WithdrawVO> findWithdrawById(@PathVariable(value = "id") Long id) {
		Withdraw withdraw = withdrawService.findWithdrawById(id);
		WithdrawVO vo = WithdrawMapper.INSTANCE.withdrawToWithdrawVO(withdraw);
		return Result.succeed(vo);
	}

}
