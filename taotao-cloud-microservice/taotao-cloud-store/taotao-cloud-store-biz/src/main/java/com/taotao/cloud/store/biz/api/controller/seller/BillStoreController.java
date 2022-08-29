package com.taotao.cloud.store.biz.api.controller.seller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.common.OperationalJudgment;
import com.taotao.cloud.common.utils.common.SecurityUtils;
import com.taotao.cloud.common.utils.servlet.RequestUtils;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.order.api.feign.IFeignStoreFlowService;
import com.taotao.cloud.order.api.web.vo.order.StoreFlowVO;
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

import javax.servlet.http.HttpServletResponse;

/**
 * 店铺端,结算单接口
 *
 * @since 2020/11/17 4:29 下午
 */
@Validated
@RestController
@Tag(name = "店铺端-结算单接口", description = "店铺端-结算单接口")
@RequestMapping("/store/bill")
public class BillStoreController {

	@Autowired
	private BillService billService;

	@Autowired
	private IFeignStoreFlowService storeFlowService;

	@Operation(summary = "获取结算单分页", description = "获取结算单分页")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/getByPage")
	public Result<PageModel<BillListVO>> getByPage(BillPageQuery billPageQuery) {
		billPageQuery.setStoreId(SecurityUtils.getCurrentUser().getStoreId());
		IPage<BillListVO> billListVOIPage = billService.billPage(billPageQuery);
		return Result.success(PageModel.convertMybatisPage(billListVOIPage, BillListVO.class));
	}

	@Operation(summary = "通过id获取结算单", description = "通过id获取结算单")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/get/{id}")
	public Result<Bill> get(@PathVariable String id) {
		return Result.success(OperationalJudgment.judgment(billService.getById(id)));
	}

	@Operation(summary = "获取商家结算单流水分页", description = "获取商家结算单流水分页")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{id}/getStoreFlow")
	public Result<PageModel<StoreFlowVO>> getStoreFlow(@PathVariable String id, @Parameter(description = "流水类型:PAY、REFUND") String flowType, PageParam pageParam) {
		OperationalJudgment.judgment(billService.getById(id));
		IPage<StoreFlowVO> storeFlow = storeFlowService.getStoreFlow(id, flowType, pageParam);
		return Result.success(PageModel.convertMybatisPage(storeFlow, StoreFlowVO.class));
	}

	@Operation(summary = "获取商家分销订单流水分页", description = "获取商家分销订单流水分页")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{id}/getDistributionFlow")
	public Result<PageModel<StoreFlowVO>> getDistributionFlow(@PathVariable String id, PageParam pageParam) {
		OperationalJudgment.judgment(billService.getById(id));
		IPage<StoreFlowVO> distributionFlow = storeFlowService.getDistributionFlow(id, pageParam);
		return Result.success(PageModel.convertMybatisPage(distributionFlow, StoreFlowVO.class));
	}

	@Operation(summary = "核对结算单", description = "核对结算单")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "/check/{id}")
	public Result<Boolean> examine(@PathVariable String id) {
		OperationalJudgment.judgment(billService.getById(id));
		billService.check(id);
		return Result.success(true);
	}

	@Operation(summary = "下载结算单", description = "下载结算单")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/downLoad/{id}")
	public void downLoadDeliverExcel(@PathVariable String id) {
		OperationalJudgment.judgment(billService.getById(id));
		HttpServletResponse response = RequestUtils.getResponse();
		billService.download(response, id);

	}

}
