package com.taotao.cloud.order.biz.controller.seller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.order.api.vo.aftersale.AfterSaleSearchParams;
import com.taotao.cloud.order.api.vo.aftersale.AfterSaleVO;
import com.taotao.cloud.order.api.vo.aftersale.AfterSaleVOVO123;
import com.taotao.cloud.order.biz.entity.aftersale.AfterSale;
import com.taotao.cloud.order.biz.service.aftersale.AfterSaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zipkin2.storage.Traces;

/**
 * 店铺端,售后管理接口
 */
@Validated
@RestController
@Tag(name = "店铺端-售后API", description = "店铺端-售后API")
@RequestMapping("/order/seller/afterSale")
public class AfterSaleController {

	@Autowired
	private AfterSaleService afterSaleService;

	@Operation(summary = "查看售后服务详情", description = "查看售后服务详情", method = CommonConstant.GET)
	@RequestLogger(description = "查看售后服务详情")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{sn}")
	public Result<AfterSaleVO> get(@PathVariable String sn) {
		AfterSaleVO afterSale = OperationalJudgment.judgment(afterSaleService.getAfterSale(sn));
		return Result.success(afterSale);
	}

	@Operation(summary = "分页获取售后服务", description = "分页获取售后服务", method = CommonConstant.GET)
	@RequestLogger(description = "分页获取售后服务")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/page")
	public Result<IPage<AfterSaleVOVO123>> getByPage(AfterSaleSearchParams searchParams) {
		String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
		searchParams.setStoreId(storeId);
		return Result.success(afterSaleService.getAfterSalePages(searchParams));
	}

	@Operation(summary = "获取导出售后服务列表列表", description = "获取导出售后服务列表列表", method = CommonConstant.GET)
	@RequestLogger(description = "获取导出售后服务列表列表")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/exportAfterSaleOrder")
	public Result<List<AfterSale>> exportAfterSaleOrder(AfterSaleSearchParams searchParams) {
		String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
		searchParams.setStoreId(storeId);
		return Result.success(afterSaleService.exportAfterSaleOrder(searchParams));
	}

	@Operation(summary = "审核售后申请", description = "审核售后申请", method = CommonConstant.POST)
	@RequestLogger(description = "审核售后申请")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping(value = "/review/{afterSaleSn}")
	public Result<AfterSale> review(
		@NotNull(message = "请选择售后单") @PathVariable String afterSaleSn,
		@NotNull(message = "请审核") String serviceStatus,
		String remark,
		BigDecimal actualRefundPrice) {

		return Result.success(
			afterSaleService.review(afterSaleSn, serviceStatus, remark, actualRefundPrice));
	}

	@Operation(summary = "卖家确认收货", description = "卖家确认收货", method = CommonConstant.PUT)
	@RequestLogger(description = "卖家确认收货")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/tree")
	@PutMapping(value = "/confirm/{afterSaleSn}")
	public Result<AfterSale> confirm(
		@NotNull(message = "请选择售后单") @PathVariable String afterSaleSn,
		@NotNull(message = "请审核") String serviceStatus,
		String remark) {

		return Result.success(afterSaleService.storeConfirm(afterSaleSn, serviceStatus, remark));
	}

	@Operation(summary = "查看买家退货物流踪迹", description = "查看买家退货物流踪迹", method = CommonConstant.GET)
	@RequestLogger(description = "查看买家退货物流踪迹")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/getDeliveryTraces/{sn}")
	public Result<Traces> getDeliveryTraces(@PathVariable String sn) {
		return Result.success(afterSaleService.deliveryTraces(sn));
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
