package com.taotao.cloud.order.biz.controller.seller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.common.OperationalJudgment;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.member.api.dto.MemberAddressDTO;
import com.taotao.cloud.order.api.dto.order.OrderExportDTO;
import com.taotao.cloud.order.api.query.order.OrderPageQuery;
import com.taotao.cloud.order.api.vo.order.OrderDetailVO;
import com.taotao.cloud.order.api.vo.order.OrderSimpleVO;
import com.taotao.cloud.order.biz.service.order.OrderPriceService;
import com.taotao.cloud.order.biz.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.UserContext;
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
	private final OrderService orderService;
	/**
	 * 订单价格
	 */
	private final OrderPriceService orderPriceService;
	/**
	 * 物流公司
	 */
	private final StoreLogisticsService storeLogisticsService;

	@Operation(summary = "查询订单列表", description = "查询订单列表")
	@RequestLogger("查询订单列表")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/page")
	public Result<IPage<OrderSimpleVO>> queryMineOrder(OrderPageQuery orderPageQuery) {
		return Result.success(orderService.queryByParams(orderPageQuery));
	}

	@Operation(summary = "订单明细", description = "订单明细")
	@RequestLogger("订单明细")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{orderSn}")
	public Result<OrderDetailVO> detail(@NotNull @PathVariable String orderSn) {
		OperationalJudgment.judgment(orderService.getBySn(orderSn));
		return Result.success(orderService.queryDetail(orderSn));
	}

	@Operation(summary = "修改收货人信息", description = "修改收货人信息")
	@RequestLogger("修改收货人信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping(value = "/update/{orderSn}/consignee")
	public Result<Object> consignee(@NotNull(message = "参数非法") @PathVariable String orderSn,
		@Valid MemberAddressDTO memberAddressDTO) {
		return Result.success(orderService.updateConsignee(orderSn, memberAddressDTO));
	}

	@Operation(summary = "修改订单价格", description = "修改订单价格")
	@RequestLogger("修改订单价格")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "/{orderSn}/price")
	public Result<Object> updateOrderPrice(@PathVariable String orderSn,
		@NotNull(message = "订单价格不能为空") @RequestParam BigDecimal orderPrice) {
		return Result.success(orderPriceService.updatePrice(orderSn, orderPrice));
	}

	@Operation(summary = "订单发货", description = "订单发货")
	@RequestLogger("订单发货")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping(value = "/{orderSn}/delivery")
	public Result<Object> delivery(@NotNull(message = "参数非法") @PathVariable String orderSn,
		@NotNull(message = "发货单号不能为空") String logisticsNo,
		@NotNull(message = "请选择物流公司") String logisticsId) {
		return Result.success(orderService.delivery(orderSn, logisticsNo, logisticsId));
	}

	@Operation(summary = "取消订单", description = "取消订单")
	@RequestLogger("取消订单")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping(value = "/{orderSn}/cancel")
	public Result<Object> cancel(@PathVariable String orderSn, @RequestParam String reason) {
		return Result.success(orderService.cancel(orderSn, reason));
	}

	@Operation(summary = "根据核验码获取订单信息", description = "根据核验码获取订单信息")
	@RequestLogger("根据核验码获取订单信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/verificationCode/{verificationCode}")
	public Result<Object> getOrderByVerificationCode(@PathVariable String verificationCode) {
		return Result.success(orderService.getOrderByVerificationCode(verificationCode));
	}

	@Operation(summary = "订单核验", description = "订单核验")
	@RequestLogger("订单核验")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "/take/{orderSn}/{verificationCode}")
	public Result<Object> take(@PathVariable String orderSn,
		@PathVariable String verificationCode) {
		return Result.success(orderService.take(orderSn, verificationCode));
	}

	@Operation(summary = "查询物流踪迹", description = "查询物流踪迹")
	@RequestLogger("查询物流踪迹")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/traces/{orderSn}")
	public Result<Object> getTraces(
		@NotBlank(message = "订单编号不能为空") @PathVariable String orderSn) {
		OperationalJudgment.judgment(orderService.getBySn(orderSn));
		return Result.success(orderService.getTraces(orderSn));
	}

	@Operation(summary = "下载待发货的订单列表", description = "下载待发货的订单列表")
	@RequestLogger("下载待发货的订单列表")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/downLoadDeliverExcel")
	public void downLoadDeliverExcel() {
		HttpServletResponse response = ThreadContextHolder.getHttpResponse();
		String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
		//获取店铺已经选择物流公司列表
		List<String> logisticsName = storeLogisticsService.getStoreSelectedLogisticsName(storeId);
		//下载订单批量发货Excel
		this.orderService.getBatchDeliverList(response, logisticsName);
	}

	@Operation(summary = "上传文件进行订单批量发货", description = "上传文件进行订单批量发货")
	@RequestLogger("上传文件进行订单批量发货")
	@PostMapping(value = "/batchDeliver", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Result<Object> batchDeliver(@RequestPart("files") MultipartFile files) {
		orderService.batchDeliver(files);
		return Result.success(ResultEnum.SUCCESS);
	}

	@Operation(summary = "查询订单导出列表", description = "查询订单导出列表")
	@RequestLogger("查询订单导出列表")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/queryExportOrder")
	public Result<List<OrderExportDTO>> queryExportOrder(
		OrderPageQuery orderPageQuery) {
		return Result.success(orderService.queryExportOrder(orderPageQuery));
	}
}
