package com.taotao.cloud.order.biz.controller.seller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.order.api.enums.order.CommunicationOwnerEnum;
import com.taotao.cloud.order.api.vo.order.OrderComplaintCommunicationVO;
import com.taotao.cloud.order.api.vo.order.OrderComplaintOperationParams;
import com.taotao.cloud.order.api.vo.order.OrderComplaintSearchParams;
import com.taotao.cloud.order.api.vo.order.OrderComplaintVO;
import com.taotao.cloud.order.api.vo.order.StoreAppealVO;
import com.taotao.cloud.order.biz.entity.order.OrderComplaint;
import com.taotao.cloud.order.biz.service.order.OrderComplaintCommunicationService;
import com.taotao.cloud.order.biz.service.order.OrderComplaintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Objects;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺端,交易投诉接口
 **/
@Validated
@RestController
@Tag(name = "店铺端-交易投诉API", description = "店铺端-交易投诉API")
@RequestMapping("/order/seller/complain")
public class OrderComplaintController {

	/**
	 * 交易投诉
	 */
	@Autowired
	private OrderComplaintService orderComplaintService;

	/**
	 * 投诉沟通
	 */
	@Autowired
	private OrderComplaintCommunicationService orderComplaintCommunicationService;

	@Operation(summary = "通过id获取", description = "通过id获取", method = CommonConstant.GET)
	@RequestLogger(description = "通过id获取")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{id}")
	public Result<OrderComplaintVO> get(@PathVariable String id) {
		return Result.success(
			OperationalJudgment.judgment(orderComplaintService.getOrderComplainById(id)));
	}

	@Operation(summary = "分页获取", description = "分页获取", method = CommonConstant.GET)
	@RequestLogger(description = "分页获取")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/page")
	public Result<IPage<OrderComplaint>> get(OrderComplaintSearchParams searchParams,
		PageVO pageVO) {
		String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
		searchParams.setStoreId(storeId);
		return Result.success(orderComplaintService.getOrderComplainByPage(searchParams, pageVO));
	}

	@Operation(summary = "添加交易投诉对话", description = "添加交易投诉对话", method = CommonConstant.POST)
	@RequestLogger(description = "添加交易投诉对话")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping("/communication")
	public Result<OrderComplaintCommunicationVO> addCommunication(
		@RequestParam String complainId, @RequestParam String content) {
		AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
		OrderComplaintCommunicationVO communicationVO = new OrderComplaintCommunicationVO(
			complainId, content, CommunicationOwnerEnum.STORE.name(), currentUser.getStoreId(),
			currentUser.getUsername());
		orderComplaintCommunicationService.addCommunication(communicationVO);
		return ResultUtil.success();
	}

	@Operation(summary = "修改申诉信息", description = "修改申诉信息", method = CommonConstant.PUT)
	@RequestLogger(description = "修改申诉信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping
	public Result<OrderComplaintVO> update(OrderComplaintVO orderComplainVO) {
		String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
		orderComplainVO.setStoreId(storeId);
		orderComplaintService.updateOrderComplain(orderComplainVO);
		return Result.success(orderComplainVO);
	}

	@Operation(summary = "申诉", description = "申诉", method = CommonConstant.POST)
	@RequestLogger(description = "申诉")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping("/appeal")
	public Result<OrderComplaintVO> appeal(StoreAppealVO storeAppealVO) {
		orderComplaintService.appeal(storeAppealVO);
		return Result.success(
			orderComplaintService.getOrderComplainById(storeAppealVO.getOrderComplaintId()));
	}
	
	@Operation(summary = "修改状态", description = "修改状态", method = CommonConstant.PUT)
	@RequestLogger(description = "修改状态")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "/status")
	public Result<Object> updateStatus(OrderComplaintOperationParams orderComplainVO) {
		orderComplaintService.updateOrderComplainByStatus(orderComplainVO);
		return ResultUtil.success();
	}

}
