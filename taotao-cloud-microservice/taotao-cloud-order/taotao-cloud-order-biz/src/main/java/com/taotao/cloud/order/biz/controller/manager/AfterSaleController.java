package com.taotao.cloud.order.biz.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.order.api.vo.aftersale.AfterSaleSearchParams;
import com.taotao.cloud.order.api.vo.aftersale.AfterSaleVO;
import com.taotao.cloud.order.biz.entity.aftersale.AfterSale;
import com.taotao.cloud.order.biz.service.aftersale.AfterSaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zipkin2.storage.Traces;

/**
 * 管理端,售后接口
 */
@Validated
@RestController
@Tag(name = "平台管理端-售后管理API", description = "平台管理端-售后管理API")
@RequestMapping("/order/manager/afterSale")
public class AfterSaleController {

	/**
	 * 售后
	 */
	@Autowired
	private AfterSaleService afterSaleService;

	@Operation(summary = "分页获取售后服务", description = "分页获取售后服务", method = CommonConstant.GET)
	@RequestLogger(description = "分页获取售后服务")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/page")
	public Result<IPage<AfterSaleVOVO123>> getByPage(AfterSaleSearchParams searchParams) {
		return Result.success(afterSaleService.getAfterSalePages(searchParams));
	}

	@Operation(summary = "获取导出售后服务列表列表", description = "获取导出售后服务列表列表", method = CommonConstant.GET)
	@RequestLogger(description = "获取导出售后服务列表列表")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/exportAfterSaleOrder")
	public Result<List<AfterSale>> exportAfterSaleOrder(AfterSaleSearchParams searchParams) {
		return Result.success(afterSaleService.exportAfterSaleOrder(searchParams));
	}

	@Operation(summary = "查看售后服务详情", description = "查看售后服务详情", method = CommonConstant.GET)
	@RequestLogger(description = "查看售后服务详情")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{sn}")
	public Result<AfterSaleVO> get(
		@NotNull(message = "售后单号") @PathVariable("sn") String sn) {
		return Result.success(afterSaleService.getAfterSale(sn));
	}

	@Operation(summary = "查看买家退货物流踪迹", description = "查看买家退货物流踪迹", method = CommonConstant.GET)
	@RequestLogger(description = "查看买家退货物流踪迹")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/delivery/traces/{sn}")
	public Result<Traces> getDeliveryTraces(@PathVariable String sn) {
		return Result.success(afterSaleService.deliveryTraces(sn));
	}

	@Operation(summary = "售后线下退款", description = "售后线下退款", method = CommonConstant.PUT)
	@RequestLogger(description = "售后线下退款")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "/refund/{afterSaleSn}")
	public Result<AfterSale> refund(
		@NotNull(message = "请选择售后单") @PathVariable String afterSaleSn,
		@RequestParam String remark) {
		return Result.success(afterSaleService.refund(afterSaleSn, remark));
	}

	@Operation(summary = "审核售后申请", description = "审核售后申请", method = CommonConstant.PUT)
	@RequestLogger(description = "审核售后申请")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "/review/{afterSaleSn}")
	public Result<AfterSale> review(
		@NotNull(message = "请选择售后单") @PathVariable String afterSaleSn,
		@NotNull(message = "请审核") String serviceStatus,
		String remark, BigDecimal actualRefundPrice) {

		return Result.success(
			afterSaleService.review(afterSaleSn, serviceStatus, remark, actualRefundPrice));
	}
	
	@Operation(summary = "获取商家售后收件地址", description = "获取商家售后收件地址", method = CommonConstant.GET)
	@RequestLogger(description = "获取商家售后收件地址")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/getStoreAfterSaleAddress/{sn}")
	public Result<StoreAfterSaleAddressDTO> getStoreAfterSaleAddress(
		@NotNull(message = "售后单号") @PathVariable("sn") String sn) {
		return Result.success(afterSaleService.getStoreAfterSaleAddressDTO(sn));
	}
}
