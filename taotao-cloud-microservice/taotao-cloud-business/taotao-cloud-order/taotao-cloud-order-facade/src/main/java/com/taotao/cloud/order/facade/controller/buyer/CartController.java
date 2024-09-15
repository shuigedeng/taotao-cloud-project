/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.order.facade.controller.buyer;

import com.taotao.boot.common.model.Result;
import com.taotao.cloud.order.api.enums.cart.CartTypeEnum;
import com.taotao.cloud.order.application.service.cart.ICartService;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
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
 * 买家端，购物车API
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:56:54
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "买家端-购物车API", description = "买家端-购物车API")
@RequestMapping("/order/buyer/cart")
public class CartController {

	/**
	 * 购物车
	 */
	private final ICartService cartService;

	@Operation(summary = "向购物车中添加一个产品", description = "向购物车中添加一个产品")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping
	public Result<Boolean> add(
		@NotNull(message = "产品id不能为空") String skuId,
		@NotNull(message = "购买数量不能为空") @Min(value = 1, message = "加入购物车数量必须大于0") Integer num,
		@NotBlank(message = "购物车类型") String cartType) {
		return Result.success(cartService.add(skuId, num, cartType, false));
	}

	@Operation(summary = "获取购物车页面购物车详情", description = "获取购物车页面购物车详情")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/all")
	public Result<TradeDTO> cartAll() {
		return Result.success(this.cartService.getAllTradeDTO());
	}

	@Operation(summary = "获取购物车数量", description = "获取购物车数量")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/count")
	public Result<Long> cartCount(@RequestParam(required = false) Boolean checked) {
		return Result.success(this.cartService.getCartNum(checked));
	}

	@Operation(summary = "获取购物车可用优惠券数量", description = "获取购物车可用优惠券数量")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/coupon/num")
	public Result<Long> cartCouponNum(@RequestParam String way) {
		return Result.success(this.cartService.getCanUseCoupon(CartTypeEnum.valueOf(way)));
	}

	@Operation(summary = "更新购物车中的多个产品的数量或选中状态", description = "更新购物车中的多个产品的数量或选中状态")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping(value = "/sku/num/{skuId}")
	public Result<Boolean> update(
		@NotNull(message = "产品id不能为空") @PathVariable(name = "skuId") String skuId, Integer num) {
		return Result.success(cartService.add(skuId, num, CartTypeEnum.CART.name(), true));
	}

	@Operation(summary = "更新购物车中单个产品 更新购物车中的多个产品的数量或选中状态", description = "更新购物车中的多个产品的数量或选中状态")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping(value = "/sku/checked/{skuId}")
	public Result<Boolean> updateChecked(
		@NotNull(message = "产品id不能为空") @PathVariable(name = "skuId") String skuId, boolean checked) {
		return Result.success(cartService.checked(skuId, checked));
	}

	@Operation(summary = "购物车选中设置", description = "购物车选中设置")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping(value = "/sku/checked", produces = MediaType.APPLICATION_JSON_VALUE)
	public Result<Boolean> updateAll(boolean checked) {
		return Result.success(cartService.checkedAll(checked));
	}

	@Operation(summary = "批量设置某商家的商品为选中或不选中", description = "批量设置某商家的商品为选中或不选中")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping(value = "/store/{storeId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Result<Boolean> updateStoreAll(
		@NotNull(message = "卖家id不能为空") @PathVariable(name = "storeId") String storeId, boolean checked) {
		return Result.success(cartService.checkedStore(storeId, checked));
	}

	@Operation(summary = "清空购物车", description = "清空购物车")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@DeleteMapping()
	public Result<Boolean> clean() {
		return Result.success(cartService.clean());
	}

	@Operation(summary = "删除购物车中的一个或多个产品", description = "删除购物车中的一个或多个产品")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@DeleteMapping(value = "/sku/remove")
	public Result<Boolean> delete(String[] skuIds) {
		return Result.success(cartService.delete(skuIds));
	}

	@Operation(summary = "获取结算页面购物车详情", description = "获取结算页面购物车详情")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/checked")
	public Result<TradeDTO> cartChecked(@NotNull(message = "读取选中列表") String way) {
		return Result.success(this.cartService.getCheckedTradeDTO(CartTypeEnum.valueOf(way)));
	}

	@Operation(summary = "选择收货地址", description = "选择收货地址")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/shippingAddress")
	public Result<Boolean> shippingAddress(@NotNull(message = "收货地址ID不能为空") String shippingAddressId, String way) {
		return Result.success(cartService.shippingAddress(shippingAddressId, way));
	}

	@Operation(summary = "选择配送方式", description = "选择配送方式")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/shippingMethod")
	public Result<Boolean> shippingMethod(
		@NotNull(message = "配送方式不能为空") String shippingMethod, String selleId, String way) {
		return Result.success(cartService.shippingMethod(selleId, shippingMethod, way));
	}

	@Operation(summary = "选择发票", description = "选择发票")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/receipt")
	public Result<Boolean> selectReceipt(String way, ReceiptVO receiptVO) {
		return Result.success(this.cartService.shippingReceipt(receiptVO, way));
	}

	@Operation(summary = "选择优惠券", description = "选择优惠券")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/coupon")
	public Result<Boolean> selectCoupon(String way,
										@NotNull(message = "优惠券id不能为空") String memberCouponId,
										boolean used) {
		return Result.success(this.cartService.selectCoupon(memberCouponId, way, used));
	}

	@Operation(summary = "创建交易", description = "创建交易")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping(value = "/trade")
	public Result<Trade> crateTrade(@RequestBody TradeDTO tradeDTO) {
		// 读取选中的列表
		return Result.success(this.cartService.createTrade(tradeDTO));
	}
}
