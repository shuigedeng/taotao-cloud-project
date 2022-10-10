package com.taotao.cloud.store.biz.api.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.order.api.feign.IFeignStoreFlowApi;
import com.taotao.cloud.order.api.model.vo.order.StoreFlowVO;
import com.taotao.cloud.store.api.web.query.BillPageQuery;
import com.taotao.cloud.store.api.web.vo.BillListVO;
import com.taotao.cloud.store.biz.model.entity.Bill;
import com.taotao.cloud.store.biz.service.BillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * 管理端,商家结算单接口
 */
@Validated
@Tag(name = "管理端-商家结算单接口", description = "管理端-商家结算单接口")
@RestController
@RequestMapping("/manager/store/bill")
public class BillManagerController {

	@Autowired
	private BillService billService;

	@Autowired
	private IFeignStoreFlowApi storeFlowService;

	@Operation(summary = "通过id获取结算单", description = "通过id获取结算单")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/get/{id}")
	public Result<Bill> get(@Parameter(description = "结算单ID") @PathVariable @NotNull String id) {
		return Result.success(billService.getById(id));
	}

	@Operation(summary = "获取结算单分页", description = "获取结算单分页")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/getByPage")
	public Result<PageResult<BillListVO>> getByPage(BillPageQuery billSearchParams) {
		IPage<BillListVO> billListVOIPage = billService.billPage(billSearchParams);
		return Result.success(PageResult.convertMybatisPage(billListVOIPage, BillListVO.class));
	}

	@Operation(summary = "获取商家结算单流水分页", description = "获取商家结算单流水分页")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{id}/getStoreFlow")
	public Result<PageResult<StoreFlowVO>> getStoreFlow(@Parameter(description = "结算单ID") @PathVariable String id,
														@Parameter(description = "流水类型:PAY、REFUND") String flowType,
														PageParam pageParam) {
		IPage<StoreFlowVO> storeFlow = storeFlowService.getStoreFlow(id, flowType, pageParam);
		return Result.success(PageResult.convertMybatisPage(storeFlow, StoreFlowVO.class));
	}

	@Operation(summary = "支付结算单", description = "支付结算单")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "/pay/{id}")
	public Result<Boolean> pay(@Parameter(description = "结算单ID") @PathVariable String id) {

		return Result.success(billService.complete(id));
	}

}
