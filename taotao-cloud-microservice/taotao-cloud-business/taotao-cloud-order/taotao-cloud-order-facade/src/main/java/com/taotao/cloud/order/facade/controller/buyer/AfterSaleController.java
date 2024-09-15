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

package com.taotao.cloud.order.facade.controller.buyer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.PageResult;
import com.taotao.boot.common.model.Result;
import com.taotao.boot.data.mybatis.mybatisplus.MpUtils;
import com.taotao.cloud.order.application.command.aftersale.dto.clientobject.AfterSaleApplyCO;
import com.taotao.cloud.order.application.command.aftersale.dto.AfterSaleAddCmd;
import com.taotao.cloud.order.application.command.aftersale.dto.clientobject.AfterSaleLogCO;
import com.taotao.cloud.order.application.command.aftersale.dto.AfterSalePageQry;
import com.taotao.cloud.order.application.command.aftersale.dto.clientobject.AfterSaleReasonCO;
import com.taotao.cloud.order.application.command.aftersale.dto.clientobject.AfterSaleCO;
import com.taotao.cloud.order.application.converter.AfterSaleConvert;
import com.taotao.cloud.order.application.converter.AfterSaleLogConvert;
import com.taotao.cloud.order.application.converter.AfterSaleReasonConvert;
import com.taotao.cloud.order.application.service.aftersale.IAfterSaleLogService;
import com.taotao.cloud.order.application.service.aftersale.IAfterSaleReasonService;
import com.taotao.cloud.order.application.service.aftersale.IAfterSaleService;
import com.taotao.cloud.order.infrastructure.persistent.po.aftersale.AfterSaleLogPO;
import com.taotao.cloud.order.infrastructure.persistent.po.aftersale.AfterSalePO;
import com.taotao.cloud.order.infrastructure.persistent.po.aftersale.AfterSaleReasonPO;
import com.taotao.boot.web.request.annotation.RequestLogger;
import com.taotao.boot.web.utils.OperationalJudgment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端,售后管理API
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-07 20:31:22
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "买家端-售后API", description = "买家端-售后API")
@RequestMapping("/order/buyer/aftersale")
public class AfterSaleController {

	/**
	 * 售后
	 */
	private final IAfterSaleService afterSaleService;
	/**
	 * 售后原因
	 */
	private final IAfterSaleReasonService afterSaleReasonService;
	/**
	 * 售后日志
	 */
	private final IAfterSaleLogService afterSaleLogService;

	@Operation(summary = "查看售后服务详情", description = "查看售后服务详情")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{sn}")
	public Result<AfterSaleCO> getAfterSaleBySn(
		@NotBlank(message = "售后单号不能为空") @PathVariable String sn) {
		AfterSalePO afterSale = OperationalJudgment.judgment(afterSaleService.getAfterSaleBySn(sn));
		return Result.success(AfterSaleConvert.INSTANCE.convert(afterSale));
	}

	@Operation(summary = "分页获取售后服务", description = "分页获取售后服务")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/page")
	public Result<PageResult<AfterSaleCO>> pageQuery(
		@Validated AfterSalePageQry afterSalePageQry) {
		IPage<AfterSalePO> afterSalePages = afterSaleService.pageQuery(afterSalePageQry);
		return Result.success(MpUtils.convertMybatisPage(afterSalePages, AfterSaleCO.class));
	}

	@Operation(summary = "获取申请售后页面信息", description = "获取申请售后页面信息")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/apply-aftersale-info/{sn}")
	public Result<AfterSaleApplyCO> applyAfterSaleInfo(
		@NotBlank(message = "售后单号不能为空") @PathVariable String sn) {
		return Result.success(afterSaleService.getAfterSaleVO(sn));
	}

	@Operation(summary = "申请售后", description = "申请售后")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping(value = "/{orderItemSn}")
	public Result<Boolean> saveAfterSale(
		@NotBlank(message = "售后单号不能为空") @PathVariable String orderItemSn,
		@Validated @RequestBody AfterSaleAddCmd afterSaleAddCmd) {
		afterSaleAddCmd = AfterSaleDTOBuilder.builder(afterSaleAddCmd)
			.orderItemSn(orderItemSn)
			.build();
		return Result.success(afterSaleService.saveAfterSale(afterSaleAddCmd));
	}

	@Operation(summary = "买家 退回 物流信息", description = "买家 退回 物流信息")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping(value = "/delivery/{afterSaleSn}")
	public Result<AfterSalePO> delivery(
		@NotNull(message = "售后编号不能为空") @PathVariable("afterSaleSn") String afterSaleSn,
		@NotNull(message = "发货单号不能为空") @RequestParam String logisticsNo,
		@NotNull(message = "请选择物流公司") @RequestParam String logisticsId,
		@NotNull(message = "请选择发货时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")
		LocalDateTime mDeliverTime) {
		return Result.success(
			afterSaleService.buyerDelivery(afterSaleSn, logisticsNo, logisticsId, mDeliverTime));
	}

	@Operation(summary = "售后，取消售后", description = "售后，取消售后")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping(value = "/cancel/{afterSaleSn}")
	public Result<Boolean> cancel(
		@NotNull(message = "售后订单编码不能为空") @PathVariable String afterSaleSn) {
		return Result.success(afterSaleService.cancel(afterSaleSn));
	}

	@Operation(summary = "获取商家售后收件地址", description = "获取商家售后收件地址")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/getStoreAfterSaleAddress/{sn}")
	public Result<StoreAfterSaleAddressVO> getStoreAfterSaleAddress(
		@NotNull(message = "售后单号") @PathVariable("sn") String sn) {
		return Result.success(afterSaleService.getStoreAfterSaleAddressVO(sn));
	}

	@Operation(summary = "获取售后原因", description = "获取售后原因")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/afterSaleReason/{serviceType}")
	public Result<List<AfterSaleReasonCO>> getAfterSaleReason(
		@NotBlank(message = "售后类型不能为空") @PathVariable String serviceType) {
		List<AfterSaleReasonPO> afterSaleReasonPOS = afterSaleReasonService.afterSaleReasonList(
			serviceType);
		return Result.success(AfterSaleReasonConvert.INSTANCE.convert(afterSaleReasonPOS));
	}

	@Operation(summary = "获取售后日志", description = "获取售后日志")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/afterSaleLog/{sn}")
	public Result<List<AfterSaleLogCO>> getAfterSaleLog(
		@NotBlank(message = "售后单号不能为空") @PathVariable String sn) {
		List<AfterSaleLogPO> afterSaleLogPOList = afterSaleLogService.getAfterSaleLog(sn);
		return Result.success(AfterSaleLogConvert.INSTANCE.convert(afterSaleLogPOList));
	}
}
