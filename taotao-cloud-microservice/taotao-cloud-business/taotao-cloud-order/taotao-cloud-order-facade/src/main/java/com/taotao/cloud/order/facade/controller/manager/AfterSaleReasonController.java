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

package com.taotao.cloud.order.facade.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.PageResult;
import com.taotao.boot.common.model.Result;
import com.taotao.boot.data.mybatis.mybatisplus.utils.MpUtils;
import com.taotao.cloud.order.application.command.aftersale.dto.AfterSaleReasonUpdateCmd;
import com.taotao.cloud.order.application.command.aftersale.dto.AfterSaleReasonPageQry;
import com.taotao.cloud.order.application.command.aftersale.dto.clientobject.AfterSaleReasonCO;
import com.taotao.cloud.order.application.converter.AfterSaleReasonConvert;
import com.taotao.cloud.order.application.service.aftersale.IAfterSaleReasonService;
import com.taotao.cloud.order.infrastructure.persistent.po.aftersale.AfterSaleReasonPO;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
 * 管理端,售后原因API
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:57:11
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "管理端-售后原因管理API", description = "管理端-售后原因管理API")
@RequestMapping("/order/manager/aftersale/reason")
public class AfterSaleReasonController {

	/**
	 * 售后原因
	 */
	private final IAfterSaleReasonService afterSaleReasonService;

	@Operation(summary = "查看售后原因", description = "查看售后原因")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{id}")
	public Result<AfterSaleReasonCO> getById(@PathVariable String id) {
		AfterSaleReasonPO afterSaleReasonPO = afterSaleReasonService.getById(id);
		return Result.success(AfterSaleReasonConvert.INSTANCE.convert(afterSaleReasonPO));
	}

	@Operation(summary = "分页获取售后原因", description = "分页获取售后原因")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/page")
	public Result<PageResult<AfterSaleReasonCO>> getByPage(
		@Validated AfterSaleReasonPageQry afterSaleReasonPageQry) {
		IPage<AfterSaleReasonPO> page = afterSaleReasonService.pageQuery(afterSaleReasonPageQry);
		return Result.success(MpUtils.convertMybatisPage(page, AfterSaleReasonCO.class));
	}

	@Operation(summary = "添加售后原因", description = "添加售后原因")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping
	public Result<Boolean> save(@Validated @RequestBody AfterSaleReasonUpdateCmd afterSaleReasonUpdateCmd) {
		return Result.success(afterSaleReasonService.save(
			AfterSaleReasonConvert.INSTANCE.convert(afterSaleReasonUpdateCmd)));
	}

	@Operation(summary = "修改售后原因", description = "修改售后原因")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping("/{id}")
	public Result<Boolean> update(
		@Validated @RequestBody AfterSaleReasonUpdateCmd afterSaleReasonUpdateCmd,
		@PathVariable("id") Long id) {
		AfterSaleReasonPO afterSaleReasonPO = AfterSaleReasonConvert.INSTANCE.convert(
			afterSaleReasonUpdateCmd);
		afterSaleReasonPO.setId(id);
		return Result.success(afterSaleReasonService.editAfterSaleReason(afterSaleReasonPO));
	}

	@Operation(summary = "删除售后原因", description = "删除售后原因")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@DeleteMapping(value = "/{id}")
	public Result<Boolean> delAllByIds(@PathVariable String id) {
		return Result.success(afterSaleReasonService.removeById(id));
	}
}
