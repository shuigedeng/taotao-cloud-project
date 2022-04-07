package com.taotao.cloud.order.biz.controller.buyer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.order.api.dto.order.OrderComplaintDTO;
import com.taotao.cloud.order.api.enums.order.CommunicationOwnerEnum;
import com.taotao.cloud.order.api.vo.order.OrderComplaintCommunicationVO;
import com.taotao.cloud.order.api.vo.order.OrderComplaintSearchParams;
import com.taotao.cloud.order.api.vo.order.OrderComplaintVO;
import com.taotao.cloud.order.biz.entity.order.OrderComplaint;
import com.taotao.cloud.order.biz.service.order.OrderComplaintCommunicationService;
import com.taotao.cloud.order.biz.service.order.OrderComplaintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Objects;
import javax.validation.Valid;
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
 * 买家端,交易投诉接口
 **/
@Validated
@RestController
@Tag(name = "买家端-交易投诉API", description = "买家端-交易投诉API")
@RequestMapping("/order/buyer/complain")
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
		OrderComplaintVO orderComplaintVO = OperationalJudgment.judgment(
			orderComplaintService.getOrderComplainById(id));
		return Result.success(orderComplaintVO);
	}


	@Operation(summary = "分页获取", description = "分页获取", method = CommonConstant.GET)
	@RequestLogger(description = "分页获取")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/page")
	public Result<IPage<OrderComplaint>> get(OrderComplaintSearchParams searchParams,
		PageVO pageVO) {
		AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
		searchParams.setMemberId(currentUser.getId());
		return Result.success(orderComplaintService.getOrderComplainByPage(searchParams, pageVO));
	}

	@Operation(summary = "添加交易投诉", description = "添加交易投诉", method = CommonConstant.POST)
	@RequestLogger(description = "添加交易投诉")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping
	public Result<OrderComplaint> add(@Valid OrderComplaintDTO orderComplaintDTO) {
		return Result.success(orderComplaintService.addOrderComplain(orderComplaintDTO));
	}

	@Operation(summary = "添加交易投诉对话", description = "添加交易投诉对话", method = CommonConstant.POST)
	@RequestLogger(description = "添加交易投诉对话")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping("/communication")
	public Result<OrderComplaintCommunicationVO> addCommunication(
		@RequestParam String complainId, @RequestParam String content) {
		AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
		OrderComplaintCommunicationVO communicationVO = new OrderComplaintCommunicationVO(
			complainId, content, CommunicationOwnerEnum.BUYER.name(), currentUser.getId(),
			currentUser.getNickName());
		orderComplaintCommunicationService.addCommunication(communicationVO);
		return Result.success(communicationVO);
	}

	@Operation(summary = "取消售后", description = "取消售后", method = CommonConstant.PUT)
	@RequestLogger(description = "取消售后")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "/status/{id}")
	public Result<Object> cancel(@PathVariable String id) {
		orderComplaintService.cancel(id);
		return Result.success();
	}


}
