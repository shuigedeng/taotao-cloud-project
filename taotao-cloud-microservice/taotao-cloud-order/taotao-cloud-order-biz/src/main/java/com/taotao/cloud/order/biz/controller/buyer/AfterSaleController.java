package com.taotao.cloud.order.biz.controller.buyer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.common.OperationalJudgment;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.order.api.dto.aftersale.AfterSaleDTO;
import com.taotao.cloud.order.api.dto.aftersale.AfterSaleDTOBuilder;
import com.taotao.cloud.order.api.query.aftersale.AfterSalePageQuery;
import com.taotao.cloud.order.api.vo.aftersale.AfterSaleApplyVO;
import com.taotao.cloud.order.api.vo.aftersale.AfterSaleLogVO;
import com.taotao.cloud.order.api.vo.aftersale.AfterSaleReasonVO;
import com.taotao.cloud.order.api.vo.aftersale.AfterSaleVO;
import com.taotao.cloud.order.biz.entity.aftersale.AfterSale;
import com.taotao.cloud.order.biz.entity.aftersale.AfterSaleLog;
import com.taotao.cloud.order.biz.entity.aftersale.AfterSaleReason;
import com.taotao.cloud.order.biz.mapstruct.IAfterSaleLogMapStruct;
import com.taotao.cloud.order.biz.mapstruct.IAfterSaleMapStruct;
import com.taotao.cloud.order.biz.mapstruct.IAfterSaleReasonMapStruct;
import com.taotao.cloud.order.biz.service.aftersale.IAfterSaleLogService;
import com.taotao.cloud.order.biz.service.aftersale.IAfterSaleReasonService;
import com.taotao.cloud.order.biz.service.aftersale.IAfterSaleService;
import com.taotao.cloud.store.api.vo.StoreAfterSaleAddressVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

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
	public Result<AfterSaleVO> get(
		@NotBlank(message = "售后单号不能为空") @PathVariable String sn) {
		AfterSale afterSale = OperationalJudgment.judgment(afterSaleService.getAfterSale(sn));
		return Result.success(IAfterSaleMapStruct.INSTANCE.afterSaleToAfterSaleVO(afterSale));
	}

	@Operation(summary = "分页获取售后服务", description = "分页获取售后服务")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/page")
	public Result<PageModel<AfterSaleVO>> page(@Validated AfterSalePageQuery afterSalePageQuery) {
		IPage<AfterSale> afterSalePages = afterSaleService.getAfterSalePages(afterSalePageQuery);
		return Result.success(PageModel.convertMybatisPage(afterSalePages, AfterSaleVO.class));
	}

	@Operation(summary = "获取申请售后页面信息", description = "获取申请售后页面信息")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/applyAfterSaleInfo/{sn}")
	public Result<AfterSaleApplyVO> applyAfterSaleInfo(@NotBlank(message = "售后单号不能为空") @PathVariable String sn) {
		return Result.success(afterSaleService.getAfterSaleVO(sn));
	}

	@Operation(summary = "申请售后", description = "申请售后")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping(value = "/{orderItemSn}")
	public Result<Boolean> save(
		@NotBlank(message = "售后单号不能为空") @PathVariable String orderItemSn,
		@Validated @RequestBody AfterSaleDTO afterSaleDTO) {
		afterSaleDTO = AfterSaleDTOBuilder.builder(afterSaleDTO).orderItemSn(orderItemSn).build();
		return Result.success(afterSaleService.saveAfterSale(afterSaleDTO));
	}

	@Operation(summary = "买家 退回 物流信息", description = "买家 退回 物流信息")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping(value = "/delivery/{afterSaleSn}")
	public Result<AfterSale> delivery(
		@NotNull(message = "售后编号不能为空") @PathVariable("afterSaleSn") String afterSaleSn,
		@NotNull(message = "发货单号不能为空") @RequestParam String logisticsNo,
		@NotNull(message = "请选择物流公司") @RequestParam String logisticsId,
		@NotNull(message = "请选择发货时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime mDeliverTime) {
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
		return Result.success(afterSaleService.getStoreAfterSaleAddressDTO(sn));
	}

	@Operation(summary = "获取售后原因", description = "获取售后原因")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/afterSaleReason/{serviceType}")
	public Result<List<AfterSaleReasonVO>> getAfterSaleReason(
		@NotBlank(message = "售后类型不能为空") @PathVariable String serviceType) {
		List<AfterSaleReason> afterSaleReasons = afterSaleReasonService.afterSaleReasonList(serviceType);
		return Result.success(IAfterSaleReasonMapStruct.INSTANCE.afterSaleReasonsToAfterSaleReasonVOs(afterSaleReasons));
	}

	@Operation(summary = "获取售后日志", description = "获取售后日志")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/afterSaleLog/{sn}")
	public Result<List<AfterSaleLogVO>> getAfterSaleLog(@NotBlank(message = "售后单号不能为空") @PathVariable String sn) {
		List<AfterSaleLog> afterSaleLogList = afterSaleLogService.getAfterSaleLog(sn);
		return Result.success(IAfterSaleLogMapStruct.INSTANCE.afterSaleLogsToAfterSaleVOs(afterSaleLogList));
	}

}
