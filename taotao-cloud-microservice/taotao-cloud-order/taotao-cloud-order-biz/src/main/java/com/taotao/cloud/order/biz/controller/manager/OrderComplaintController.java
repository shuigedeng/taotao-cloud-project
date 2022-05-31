package com.taotao.cloud.order.biz.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.common.utils.common.SecurityUtil;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.order.api.dto.order.OrderComplaintCommunicationDTO;
import com.taotao.cloud.order.api.dto.order.OrderComplaintDTO;
import com.taotao.cloud.order.api.dto.order.OrderComplaintOperationDTO;
import com.taotao.cloud.order.api.enums.order.CommunicationOwnerEnum;
import com.taotao.cloud.order.api.enums.order.OrderComplaintStatusEnum;
import com.taotao.cloud.order.api.query.order.OrderComplaintPageQuery;
import com.taotao.cloud.order.api.vo.order.OrderComplaintBaseVO;
import com.taotao.cloud.order.api.vo.order.OrderComplaintVO;
import com.taotao.cloud.order.biz.entity.order.OrderComplaint;
import com.taotao.cloud.order.biz.entity.order.OrderComplaintCommunication;
import com.taotao.cloud.order.biz.mapstruct.IOrderComplainMapStruct;
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

/**
 * 管理端,交易投诉API
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:57:14
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "平台管理端-交易投诉管理API", description = "平台管理端-交易投诉管理API")
@RequestMapping("/order/manager/order/complain")
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
		return Result.success(orderComplaintService.getOrderComplainById(id));
	}

	@Operation(summary = "分页获取", description = "分页获取")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/page")
	public Result<PageModel<OrderComplaintBaseVO>> get(@Validated OrderComplaintPageQuery orderComplaintPageQuery) {
		IPage<OrderComplaint> orderComplainByPage = orderComplaintService.getOrderComplainByPage(orderComplaintPageQuery);
		return Result.success(PageModel.convertMybatisPage(orderComplainByPage, OrderComplaintBaseVO.class));
	}

	@Operation(summary = "更新数据", description = "更新数据")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping("/{id}")
	public Result<Boolean> update(@PathVariable Long id, @Validated @RequestBody OrderComplaintDTO orderComplaintDTO) {
		OrderComplaint orderComplaint = IOrderComplainMapStruct.INSTANCE.orderComplaintDTOToOrderComplaint(orderComplaintDTO);
		orderComplaint.setId(id);
		return Result.success(orderComplaintService.updateOrderComplain(orderComplaint));
	}

	@Operation(summary = "添加交易投诉对话", description = "添加交易投诉对话")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping("/communication/{complainId}")
	public Result<Boolean> addCommunication(@PathVariable("complainId") Long complainId,
											@Validated @RequestBody OrderComplaintCommunicationDTO orderComplaintCommunicationDTO) {
		SecurityUser user = SecurityUtil.getCurrentUser();
		OrderComplaintCommunication orderComplaintCommunication = OrderComplaintCommunication.builder()
			.complainId(complainId)
			.content(orderComplaintCommunicationDTO.content())
			.owner(CommunicationOwnerEnum.PLATFORM.name())
			.ownerName(user.getUsername())
			.ownerId(user.getUserId())
			.build();
		return Result.success(orderComplaintCommunicationService.addCommunication(orderComplaintCommunication));
	}

	@Operation(summary = "修改状态", description = "修改状态")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "/status")
	public Result<Boolean> updateStatus(@Validated @RequestBody OrderComplaintOperationDTO orderComplaintOperationDTO) {
		return Result.success(orderComplaintService.updateOrderComplainByStatus(orderComplaintOperationDTO));
	}

	@Operation(summary = "仲裁", description = "仲裁")
	@RequestLogger
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
		return Result.success();
	}
}
