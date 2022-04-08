package com.taotao.cloud.order.biz.controller.buyer;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.order.api.dto.cart.TradeDTO;
import com.taotao.cloud.order.api.enums.cart.CartTypeEnum;
import com.taotao.cloud.order.api.vo.cart.TradeParams;
import com.taotao.cloud.order.api.vo.order.ReceiptVO;
import com.taotao.cloud.order.biz.service.cart.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端，购物车接口
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "买家端-购物车API", description = "买家端-购物车API")
@RequestMapping("/order/buyer/carts")
public class CartController {

	/**
	 * 购物车
	 */
	private final CartService cartService;

	@Operation(summary = "向购物车中添加一个产品", description = "向购物车中添加一个产品", method = CommonConstant.POST)
	@RequestLogger("向购物车中添加一个产品")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping
	public Result<Object> add(@NotNull(message = "产品id不能为空") String skuId,
		@NotNull(message = "购买数量不能为空") @Min(value = 1, message = "加入购物车数量必须大于0") Integer num,
		String cartType) {
		try {
			//读取选中的列表
			cartService.add(skuId, num, cartType, false);
			return Result.success();
		} catch (ServiceException se) {
			log.info(se.getMsg(), se);
			throw se;
		} catch (Exception e) {
			log.error(ResultEnum.CART_ERROR.message(), e);
			throw new BusinessException(ResultEnum.CART_ERROR);
		}
	}

	@Operation(summary = "获取购物车页面购物车详情", description = "获取购物车页面购物车详情", method = CommonConstant.GET)
	@RequestLogger("获取购物车页面购物车详情")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/all")
	public Result<TradeDTO> cartAll() {
		return Result.success(this.cartService.getAllTradeDTO());
	}

	@Operation(summary = "获取购物车数量", description = "获取购物车数量", method = CommonConstant.GET)
	@RequestLogger("获取购物车数量")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/count")
	public Result<Long> cartCount(@RequestParam(required = false) Boolean checked) {
		return Result.success(this.cartService.getCartNum(checked));
	}

	@Operation(summary = "获取购物车可用优惠券数量", description = "获取购物车可用优惠券数量", method = CommonConstant.GET)
	@RequestLogger("获取购物车可用优惠券数量")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/coupon/num")
	public Result<Long> cartCouponNum(String way) {
		return Result.success(this.cartService.getCanUseCoupon(CartTypeEnum.valueOf(way)));
	}

	@Operation(summary = "更新购物车中的多个产品的数量或选中状态", description = "更新购物车中的多个产品的数量或选中状态", method = CommonConstant.POST)
	@RequestLogger("更新购物车中的多个产品的数量或选中状态")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping(value = "/sku/num/{skuId}")
	public Result<Object> update(
		@NotNull(message = "产品id不能为空") @PathVariable(name = "skuId") String skuId,
		Integer num) {
		cartService.add(skuId, num, CartTypeEnum.CART.name(), true);
		return Result.success();
	}

	@Operation(summary = "更新购物车中单个产品 更新购物车中的多个产品的数量或选中状态", description = "更新购物车中的多个产品的数量或选中状态", method = CommonConstant.POST)
	@RequestLogger("更新购物车中的多个产品的数量或选中状态")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping(value = "/sku/checked/{skuId}")
	public Result<Object> updateChecked(
		@NotNull(message = "产品id不能为空") @PathVariable(name = "skuId") String skuId,
		boolean checked) {
		cartService.checked(skuId, checked);
		return Result.success();
	}

	@Operation(summary = "购物车选中设置", description = "购物车选中设置", method = CommonConstant.POST)
	@RequestLogger("购物车选中设置")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping(value = "/sku/checked", produces = MediaType.APPLICATION_JSON_VALUE)
	public Result<Object> updateAll(boolean checked) {
		cartService.checkedAll(checked);
		return Result.success();
	}

	@Operation(summary = "批量设置某商家的商品为选中或不选中", description = "批量设置某商家的商品为选中或不选中", method = CommonConstant.POST)
	@RequestLogger("批量设置某商家的商品为选中或不选中")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping(value = "/store/{storeId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Result<Object> updateStoreAll(
		@NotNull(message = "卖家id不能为空") @PathVariable(name = "storeId") String storeId,
		boolean checked) {
		cartService.checkedStore(storeId, checked);
		return Result.success();
	}

	@Operation(summary = "清空购物车", description = "清空购物车", method = CommonConstant.DELETE)
	@RequestLogger("清空购物车")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@DeleteMapping()
	public Result<Object> clean() {
		cartService.clean();
		return Result.success();
	}

	@Operation(summary = "删除购物车中的一个或多个产品", description = "删除购物车中的一个或多个产品", method = CommonConstant.DELETE)
	@RequestLogger("删除购物车中的一个或多个产品")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@DeleteMapping(value = "/sku/remove")
	public Result<Object> delete(String[] skuIds) {
		cartService.delete(skuIds);
		return Result.success();
	}

	@Operation(summary = "获取结算页面购物车详情", description = "获取结算页面购物车详情", method = CommonConstant.DELETE)
	@RequestLogger("获取结算页面购物车详情")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/checked")
	public Result<TradeDTO> cartChecked(@NotNull(message = "读取选中列表") String way) {
		try {
			//读取选中的列表
			return Result.success(this.cartService.getCheckedTradeDTO(CartTypeEnum.valueOf(way)));
		} catch (ServiceException se) {
			log.error(se.getMsg(), se);
			throw se;
		} catch (Exception e) {
			log.error(ResultEnum.CART_ERROR.message(), e);
			throw new BusinessException(ResultEnum.CART_ERROR);
		}
	}

	@Operation(summary = "选择收货地址", description = "选择收货地址", method = CommonConstant.DELETE)
	@RequestLogger("选择收货地址")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/shippingAddress")
	public Result<Object> shippingAddress(
		@NotNull(message = "收货地址ID不能为空") String shippingAddressId, String way) {
		try {
			cartService.shippingAddress(shippingAddressId, way);
			return Result.success();
		} catch (ServiceException se) {
			log.error(ResultEnum.SHIPPING_NOT_APPLY.message(), se);
			throw new BusinessException(ResultEnum.SHIPPING_NOT_APPLY);
		} catch (Exception e) {
			log.error(ResultEnum.CART_ERROR.message(), e);
			throw new BusinessException(ResultEnum.CART_ERROR);
		}
	}

	@Operation(summary = "选择配送方式", description = "选择配送方式", method = CommonConstant.DELETE)
	@RequestLogger("选择配送方式")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/shippingMethod")
	public Result<Object> shippingMethod(
		@NotNull(message = "配送方式不能为空") String shippingMethod,
		String selleId,
		String way) {
		try {
			cartService.shippingMethod(selleId, shippingMethod, way);
			return Result.success();
		} catch (ServiceException se) {
			log.error(se.getMsg(), se);
			throw se;
		} catch (Exception e) {
			log.error(ResultEnum.CART_ERROR.message(), e);
			throw new BusinessException(ResultEnum.CART_ERROR);
		}
	}

	@Operation(summary = "选择发票", description = "选择发票", method = CommonConstant.GET)
	@RequestLogger("选择发票")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/receipt")
	public Result<Object> selectReceipt(String way, ReceiptVO receiptVO) {
		this.cartService.shippingReceipt(receiptVO, way);
		return Result.success();
	}

	@Operation(summary = "选择优惠券", description = "选择优惠券", method = CommonConstant.GET)
	@RequestLogger("选择优惠券")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/coupon")
	public Result<Object> selectCoupon(String way,
		@NotNull(message = "优惠券id不能为空") String memberCouponId, boolean used) {
		this.cartService.selectCoupon(memberCouponId, way, used);
		return Result.success();
	}

	@Operation(summary = "创建交易", description = "创建交易", method = CommonConstant.POST)
	@RequestLogger("创建交易")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping(value = "/trade", consumes = "application/json", produces = "application/json")
	public Result<Object> crateTrade(@RequestBody TradeParams tradeParams) {
		try {
			//读取选中的列表
			return Result.success(this.cartService.createTrade(tradeParams));
		} catch (ServiceException se) {
			log.info(se.getMsg(), se);
			throw se;
		} catch (Exception e) {
			log.error(ResultEnum.ORDER_ERROR.message(), e);
			throw new BusinessException(ResultEnum.ORDER_ERROR);
		}
	}
}
