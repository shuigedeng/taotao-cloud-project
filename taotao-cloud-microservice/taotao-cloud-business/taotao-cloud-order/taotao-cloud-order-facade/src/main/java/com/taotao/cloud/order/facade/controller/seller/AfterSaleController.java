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

package com.taotao.cloud.order.facade.controller.seller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.PageResult;
import com.taotao.boot.common.model.Result;
import com.taotao.cloud.order.application.command.aftersale.dto.AfterSalePageQry;
import com.taotao.cloud.order.application.command.aftersale.dto.clientobject.AfterSaleCO;
import com.taotao.cloud.order.application.converter.AfterSaleConvert;
import com.taotao.cloud.order.application.service.aftersale.IAfterSaleService;
import com.taotao.cloud.order.infrastructure.persistent.po.aftersale.AfterSalePO;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.boot.web.request.annotation.RequestLogger;
import com.taotao.boot.web.utils.OperationalJudgment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺端,售后管理API
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:57:33
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "店铺端-售后API", description = "店铺端-售后API")
@RequestMapping("/order/seller/aftersale")
public class AfterSaleController {

	private final IAfterSaleService afterSaleService;

	@Operation(summary = "查看售后服务详情", description = "查看售后服务详情")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{sn}")
	public Result<AfterSaleCO> getAfterSaleBySn(@PathVariable String sn) {
		AfterSalePO afterSale = OperationalJudgment.judgment(afterSaleService.getAfterSaleBySn(sn));
		return Result.success(AfterSaleConvert.INSTANCE.convert(afterSale));
	}

	@Operation(summary = "分页获取售后服务", description = "分页获取售后服务")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/page")
	public Result<PageResult<AfterSaleCO>> getByPage(AfterSalePageQry searchParams) {
		Long storeId = SecurityUtils.getCurrentUser().getStoreId();
		searchParams.setStoreId(storeId);
		IPage<AfterSalePO> page = afterSaleService.pageQuery(searchParams);
		return Result.success(MpUtils.convertMybatisPage(page, AfterSaleCO.class));
	}

	@Operation(summary = "获取导出售后服务列表列表", description = "获取导出售后服务列表列表")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/exportAfterSaleOrder")
	public Result<List<AfterSaleCO>> exportAfterSaleOrder(AfterSalePageQry searchParams) {
		Long storeId = SecurityUtils.getCurrentUser().getStoreId();
		searchParams.setStoreId(storeId);
		List<AfterSalePO> afterSales = afterSaleService.exportAfterSaleOrder(searchParams);
		return Result.success(AfterSaleConvert.INSTANCE.convert(afterSales));
	}

	@Operation(summary = "审核售后申请", description = "审核售后申请")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping(value = "/review/{afterSaleSn}")
	public Result<Boolean> review(
		@NotNull(message = "请选择售后单") @PathVariable String afterSaleSn,
		@NotNull(message = "请审核") String serviceStatus,
		String remark,
		BigDecimal actualRefundPrice) {

		return Result.success(
			afterSaleService.review(afterSaleSn, serviceStatus, remark, actualRefundPrice));
	}

	@Operation(summary = "卖家确认收货", description = "卖家确认收货")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/tree")
	@PutMapping(value = "/confirm/{afterSaleSn}")
	public Result<Boolean> confirm(
		@NotNull(message = "请选择售后单") @PathVariable String afterSaleSn,
		@NotNull(message = "请审核") String serviceStatus,
		String remark) {
		return Result.success(afterSaleService.storeConfirm(afterSaleSn, serviceStatus, remark));
	}

	@Operation(summary = "查看买家退货物流踪迹", description = "查看买家退货物流踪迹")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/getDeliveryTraces/{sn}")
	public Result<TracesVO> getDeliveryTraces(@PathVariable String sn) {
		return Result.success(afterSaleService.deliveryTraces(sn));
	}

	@Operation(summary = "获取商家售后收件地址", description = "获取商家售后收件地址")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/getStoreAfterSaleAddress/{sn}")
	public Result<StoreAfterSaleAddressVO> getStoreAfterSaleAddress(
		@NotNull(message = "售后单号") @PathVariable("sn") String sn) {
		return Result.success(afterSaleService.getStoreAfterSaleAddressVO(sn));
	}
}
