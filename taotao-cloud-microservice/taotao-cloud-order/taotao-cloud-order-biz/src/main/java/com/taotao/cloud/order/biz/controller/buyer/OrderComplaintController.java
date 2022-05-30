package com.taotao.cloud.order.biz.controller.buyer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.common.utils.common.OperationalJudgment;
import com.taotao.cloud.common.utils.common.SecurityUtil;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.order.api.dto.order.OrderComplaintCommunicationDTO;
import com.taotao.cloud.order.api.dto.order.OrderComplaintDTO;
import com.taotao.cloud.order.api.enums.order.CommunicationOwnerEnum;
import com.taotao.cloud.order.api.query.order.OrderComplaintPageQuery;
import com.taotao.cloud.order.api.vo.order.OrderComplaintBaseVO;
import com.taotao.cloud.order.api.vo.order.OrderComplaintVO;
import com.taotao.cloud.order.biz.entity.order.OrderComplaint;
import com.taotao.cloud.order.biz.entity.order.OrderComplaintCommunication;
import com.taotao.cloud.order.biz.service.order.IOrderComplaintCommunicationService;
import com.taotao.cloud.order.biz.service.order.IOrderComplaintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 买家端,交易投诉API
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:56:57
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "买家端-交易投诉API", description = "买家端-交易投诉API")
@RequestMapping("/order/buyer/order/complain")
public class OrderComplaintController {

	/**
	 * 交易投诉
	 */
	private final IOrderComplaintService orderComplaintService;

	/**
	 * 交易投诉沟通
	 */
	private final IOrderComplaintCommunicationService orderComplaintCommunicationService;

	@Operation(summary = "通过id获取", description = "通过id获取")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{id}")
	public Result<OrderComplaintVO> get(@PathVariable Long id) {
		OrderComplaintVO orderComplaintVO = OperationalJudgment.judgment(
			orderComplaintService.getOrderComplainById(id));
		return Result.success(orderComplaintVO);
	}


	@Operation(summary = "分页获取", description = "分页获取")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/page")
	public Result<PageModel<OrderComplaintBaseVO>> get(@Validated OrderComplaintPageQuery orderComplaintPageQuery) {
		IPage<OrderComplaint> orderComplainByPage = orderComplaintService.getOrderComplainByPage(orderComplaintPageQuery);
		return Result.success(PageModel.convertMybatisPage(orderComplainByPage, OrderComplaintBaseVO.class));
	}

	@Operation(summary = "添加交易投诉", description = "添加交易投诉")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping
	public Result<OrderComplaint> add(@Valid OrderComplaintDTO orderComplaintDTO) {
		return Result.success(orderComplaintService.addOrderComplain(orderComplaintDTO));
	}

	@Operation(summary = "添加交易投诉对话", description = "添加交易投诉对话")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping("/communication/{complainId}")
	public Result<Boolean> addCommunication(@PathVariable("complainId") Long complainId,
																  @Validated @RequestBody OrderComplaintCommunicationDTO orderComplaintCommunicationDTO) {
		SecurityUser user = SecurityUtil.getUser();
		OrderComplaintCommunication orderComplaintCommunication = OrderComplaintCommunication.builder()
			.complainId(complainId)
			.content(orderComplaintCommunicationDTO.content())
			.owner(CommunicationOwnerEnum.BUYER.name())
			.ownerName(user.getNickname())
			.ownerId(user.getUserId())
			.build();

		return Result.success(orderComplaintCommunicationService.addCommunication(orderComplaintCommunication));
	}

	@Operation(summary = "取消售后", description = "取消售后")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "/status/{id}")
	public Result<Boolean> cancel(@PathVariable Long id) {
		return Result.success(orderComplaintService.cancel(id));
	}


}
