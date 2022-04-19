package com.taotao.cloud.order.biz.controller.seller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.common.utils.common.OperationalJudgment;
import com.taotao.cloud.common.utils.common.SecurityUtil;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.order.api.dto.order.*;
import com.taotao.cloud.order.api.enums.order.CommunicationOwnerEnum;
import com.taotao.cloud.order.api.vo.order.OrderComplaintBaseVO;
import com.taotao.cloud.order.api.vo.order.OrderComplaintVO;
import com.taotao.cloud.order.biz.entity.order.OrderComplaint;
import com.taotao.cloud.order.biz.entity.order.OrderComplaintCommunication;
import com.taotao.cloud.order.biz.mapstruct.IOrderComplainMapStruct;
import com.taotao.cloud.order.biz.service.order.OrderComplaintCommunicationService;
import com.taotao.cloud.order.biz.service.order.OrderComplaintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 店铺端,交易投诉API
 **/
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "店铺端-交易投诉API", description = "店铺端-交易投诉API")
@RequestMapping("/order/seller/order/complain")
public class OrderComplaintController {

	/**
	 * 交易投诉
	 */
	private final OrderComplaintService orderComplaintService;

	/**
	 * 投诉沟通
	 */
	private final OrderComplaintCommunicationService orderComplaintCommunicationService;

	@Operation(summary = "通过id获取", description = "通过id获取")
	@RequestLogger("通过id获取")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{id}")
	public Result<OrderComplaintVO> get(@PathVariable Long id) {
		return Result.success(
			OperationalJudgment.judgment(orderComplaintService.getOrderComplainById(id)));
	}

	@Operation(summary = "分页获取", description = "分页获取")
	@RequestLogger("分页获取")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/page")
	public Result<PageModel<OrderComplaintBaseVO>> get(OrderComplaintPageQuery orderComplaintPageQuery) {
		Long storeId = SecurityUtil.getUser().getStoreId();
		orderComplaintPageQuery.setStoreId(storeId);
		IPage<OrderComplaint> orderComplainPage = orderComplaintService.getOrderComplainByPage(orderComplaintPageQuery);
		return Result.success(PageModel.convertMybatisPage(orderComplainPage, OrderComplaintBaseVO.class));
	}

	@Operation(summary = "添加交易投诉对话", description = "添加交易投诉对话")
	@RequestLogger("添加交易投诉对话")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping("/communication/{complainId}")
	public Result<Boolean> addCommunication(@PathVariable("complainId") Long complainId,
											@Validated @RequestBody OrderComplaintCommunicationDTO orderComplaintCommunicationDTO) {
		SecurityUser user = SecurityUtil.getUser();
		OrderComplaintCommunication orderComplaintCommunication = OrderComplaintCommunication.builder()
			.complainId(complainId)
			.content(orderComplaintCommunicationDTO.getContent())
			.owner(CommunicationOwnerEnum.STORE.name())
			.ownerName(user.getUsername())
			.ownerId(user.getStoreId())
			.build();
		return Result.success(orderComplaintCommunicationService.addCommunication(orderComplaintCommunication));
	}

	@Operation(summary = "修改申诉信息", description = "修改申诉信息")
	@RequestLogger("修改申诉信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping("/{id}")
	public Result<Boolean> update(@PathVariable Long id, @Validated @RequestBody OrderComplaintDTO orderComplaintDTO) {
		Long storeId =  SecurityUtil.getUser().getStoreId();
		OrderComplaint orderComplaint = IOrderComplainMapStruct.INSTANCE.orderComplaintDTOToOrderComplaint(orderComplaintDTO);
		orderComplaint.setStoreId(storeId);
		return Result.success(orderComplaintService.updateOrderComplain(orderComplaint));
	}

	@Operation(summary = "申诉", description = "申诉")
	@RequestLogger("申诉")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping("/appeal")
	public Result<OrderComplaintVO> appeal( @Validated @RequestBody StoreAppealDTO storeAppealDTO) {
		orderComplaintService.appeal(storeAppealDTO);
		return Result.success(
			orderComplaintService.getOrderComplainById(storeAppealDTO.getOrderComplaintId()));
	}

	@Operation(summary = "修改状态", description = "修改状态")
	@RequestLogger("修改状态")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "/status")
	public Result<Boolean> updateStatus( @Validated @RequestBody OrderComplaintOperationDTO orderComplaintOperationDTO) {
		return Result.success(orderComplaintService.updateOrderComplainByStatus(orderComplaintOperationDTO));
	}

}
