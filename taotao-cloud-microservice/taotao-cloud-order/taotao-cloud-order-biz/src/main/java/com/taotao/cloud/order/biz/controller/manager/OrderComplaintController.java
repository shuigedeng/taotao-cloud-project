package com.taotao.cloud.order.biz.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.order.api.enums.order.CommunicationOwnerEnum;
import com.taotao.cloud.order.api.enums.order.OrderComplaintStatusEnum;
import com.taotao.cloud.order.api.vo.order.OrderComplaintCommunicationVO;
import com.taotao.cloud.order.api.vo.order.OrderComplaintOperationParams;
import com.taotao.cloud.order.api.vo.order.OrderComplaintSearchParams;
import com.taotao.cloud.order.api.vo.order.OrderComplaintVO;
import com.taotao.cloud.order.biz.entity.order.OrderComplaint;
import com.taotao.cloud.order.biz.service.order.OrderComplaintCommunicationService;
import com.taotao.cloud.order.biz.service.order.OrderComplaintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
 * 管理端,交易投诉接口
 */
@Validated
@RestController
@Tag(name = "平台管理端-交易投诉管理API", description = "平台管理端-交易投诉管理API")
@RequestMapping("/order/manager/complain")
public class OrderComplaintController {

	/**
	 * 交易投诉
	 */
	@Autowired
	private OrderComplaintService orderComplaintService;

	/**
	 * 交易投诉沟通
	 */
	@Autowired
	private OrderComplaintCommunicationService orderComplaintCommunicationService;

	@Operation(summary = "通过id获取", description = "通过id获取", method = CommonConstant.GET)
	@RequestLogger(description = "通过id获取")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{id}")
	public Result<OrderComplaintVO> get(@PathVariable String id) {
		return Result.success(orderComplaintService.getOrderComplainById(id));
	}

	@Operation(summary = "分页获取", description = "分页获取", method = CommonConstant.GET)
	@RequestLogger(description = "分页获取")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/page")
	public Result<IPage<OrderComplaint>> get(OrderComplaintSearchParams searchParams,
		PageVO pageVO) {
		return Result.success(orderComplaintService.getOrderComplainByPage(searchParams, pageVO));
	}

	@Operation(summary = "更新数据", description = "更新数据", method = CommonConstant.PUT)
	@RequestLogger(description = "更新数据")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping
	public Result<OrderComplaintVO> update(OrderComplaintVO orderComplainVO) {
		orderComplaintService.updateOrderComplain(orderComplainVO);
		return Result.success(orderComplainVO);
	}

	@Operation(summary = "添加交易投诉对话", description = "添加交易投诉对话", method = CommonConstant.POST)
	@RequestLogger(description = "添加交易投诉对话")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping("/communication")
	public Result<OrderComplaintCommunicationVO> addCommunication(
		@RequestParam String complainId, @RequestParam String content) {
		AuthUser currentUser = UserContext.getCurrentUser();
		OrderComplaintCommunicationVO communicationVO = new OrderComplaintCommunicationVO(
			complainId, content, CommunicationOwnerEnum.PLATFORM.name(), currentUser.getId(),
			currentUser.getUsername());
		orderComplaintCommunicationService.addCommunication(communicationVO);
		return Result.success(communicationVO);
	}

	@Operation(summary = "修改状态", description = "修改状态", method = CommonConstant.PUT)
	@RequestLogger(description = "修改状态")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "/status")
	public Result<Object> updateStatus(OrderComplaintOperationParams orderComplainVO) {
		orderComplaintService.updateOrderComplainByStatus(orderComplainVO);
		return ResultUtil.success();
	}

	@Operation(summary = "仲裁", description = "仲裁", method = CommonConstant.PUT)
	@RequestLogger(description = "仲裁")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "/complete/{id}")
	public Result<Object> complete(@PathVariable String id, String arbitrationResult) {
		//新建对象
		OrderComplaintOperationParams orderComplaintOperationParams = new OrderComplaintOperationParams();
		orderComplaintOperationParams.setComplainId(id);
		orderComplaintOperationParams.setArbitrationResult(arbitrationResult);
		orderComplaintOperationParams.setComplainStatus(OrderComplaintStatusEnum.COMPLETE.name());

		//修改状态
		orderComplaintService.updateOrderComplainByStatus(orderComplaintOperationParams);
		return ResultUtil.success();
	}
}
