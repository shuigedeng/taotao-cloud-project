package com.taotao.cloud.order.biz.controller.business.seller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.common.OperationalJudgment;
import com.taotao.cloud.common.utils.common.SecurityUtils;
import com.taotao.cloud.common.utils.servlet.RequestUtils;
import com.taotao.cloud.member.api.model.dto.MemberAddressDTO;
import com.taotao.cloud.order.api.model.query.order.OrderPageQuery;
import com.taotao.cloud.order.api.model.vo.cart.OrderExportVO;
import com.taotao.cloud.order.api.model.vo.order.OrderDetailVO;
import com.taotao.cloud.order.api.model.vo.order.OrderSimpleVO;
import com.taotao.cloud.order.biz.service.business.order.IOrderPriceService;
import com.taotao.cloud.order.biz.service.business.order.IOrderService;
import com.taotao.cloud.store.api.feign.IFeignStoreLogisticsService;
import com.taotao.cloud.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 店铺端,订单API
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:57:38
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "店铺端-订单API", description = "店铺端-订单API")
@RequestMapping("/order/seller/order")
public class OrderController {

	/**
	 * 订单
	 */
	private final IOrderService orderService;
	/**
	 * 订单价格
	 */
	private final IOrderPriceService orderPriceService;
	/**
	 * 物流公司
	 */
	private final IFeignStoreLogisticsService storeLogisticsService;

	@Operation(summary = "查询订单列表", description = "查询订单列表")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/page")
	public Result<IPage<OrderSimpleVO>> queryMineOrder(OrderPageQuery orderPageQuery) {
		return Result.success(orderService.queryByParams(orderPageQuery));
	}

	@Operation(summary = "订单明细", description = "订单明细")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{orderSn}")
	public Result<OrderDetailVO> detail(@NotNull @PathVariable String orderSn) {
		OperationalJudgment.judgment(orderService.getBySn(orderSn));
		return Result.success(orderService.queryDetail(orderSn));
	}

	@Operation(summary = "修改收货人信息", description = "修改收货人信息")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping(value = "/update/{orderSn}/consignee")
	public Result<Object> consignee(@NotNull(message = "参数非法") @PathVariable String orderSn,
			@Valid MemberAddressDTO memberAddressDTO) {
		return Result.success(orderService.updateConsignee(orderSn, memberAddressDTO));
	}

	@Operation(summary = "修改订单价格", description = "修改订单价格")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "/{orderSn}/price")
	public Result<Object> updateOrderPrice(@PathVariable String orderSn,
			@NotNull(message = "订单价格不能为空") @RequestParam BigDecimal orderPrice) {
		return Result.success(orderPriceService.updatePrice(orderSn, orderPrice));
	}

	@Operation(summary = "订单发货", description = "订单发货")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping(value = "/{orderSn}/delivery")
	public Result<Object> delivery(@NotNull(message = "参数非法") @PathVariable String orderSn,
			@NotNull(message = "发货单号不能为空") String logisticsNo,
			@NotNull(message = "请选择物流公司") Long logisticsId) {
		return Result.success(orderService.delivery(orderSn, logisticsNo, logisticsId));
	}

	@Operation(summary = "取消订单", description = "取消订单")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping(value = "/{orderSn}/cancel")
	public Result<Object> cancel(@PathVariable String orderSn, @RequestParam String reason) {
		return Result.success(orderService.cancel(orderSn, reason));
	}

	@Operation(summary = "根据核验码获取订单信息", description = "根据核验码获取订单信息")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/verificationCode/{verificationCode}")
	public Result<Object> getOrderByVerificationCode(@PathVariable String verificationCode) {
		return Result.success(orderService.getOrderByVerificationCode(verificationCode));
	}

	@Operation(summary = "订单核验", description = "订单核验")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "/take/{orderSn}/{verificationCode}")
	public Result<Object> take(@PathVariable String orderSn,
			@PathVariable String verificationCode) {
		return Result.success(orderService.take(orderSn, verificationCode));
	}

	@Operation(summary = "查询物流踪迹", description = "查询物流踪迹")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/traces/{orderSn}")
	public Result<Object> getTraces(
			@NotBlank(message = "订单编号不能为空") @PathVariable String orderSn) {
		OperationalJudgment.judgment(orderService.getBySn(orderSn));
		return Result.success(orderService.getTraces(orderSn));
	}

	@Operation(summary = "下载待发货的订单列表", description = "下载待发货的订单列表")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/downLoadDeliverExcel")
	public void downLoadDeliverExcel() {
		//获取店铺已经选择物流公司列表
		List<String> logisticsName = storeLogisticsService.getStoreSelectedLogisticsName(
				SecurityUtils.getCurrentUser().getStoreId());
		//下载订单批量发货Excel
		this.orderService.getBatchDeliverList(RequestUtils.getResponse(), logisticsName);
	}

	@Operation(summary = "上传文件进行订单批量发货", description = "上传文件进行订单批量发货")
	@RequestLogger
	@PostMapping(value = "/batchDeliver", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Result<Object> batchDeliver(@RequestPart("files") MultipartFile files) {
		orderService.batchDeliver(files);
		return Result.success(ResultEnum.SUCCESS);
	}

	@Operation(summary = "查询订单导出列表", description = "查询订单导出列表")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/queryExportOrder")
	public Result<List<OrderExportVO>> queryExportOrder(
			OrderPageQuery orderPageQuery) {
		return Result.success(orderService.queryExportOrder(orderPageQuery));
	}
}
