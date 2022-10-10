package com.taotao.cloud.member.biz.controller.business.buyer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.member.api.model.vo.GoodsCollectionVO;
import com.taotao.cloud.member.biz.service.business.IMemberGoodsCollectionService;
import com.taotao.cloud.store.api.feign.IFeignStoreCollectionService;
import com.taotao.cloud.store.api.web.vo.StoreCollectionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * 买家端,会员收藏API
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "买家端-会员收藏API", description = "买家端-会员收藏API")
@RequestMapping("/member/buyer/member/collection")
public class MemberCollectionController {

	/**
	 * 会员商品收藏
	 */
	private final IMemberGoodsCollectionService IMemberGoodsCollectionService;
	/**
	 * 会员店铺
	 */
	private final IFeignStoreCollectionService storeCollectionService;
	/**
	 * 商品收藏关键字
	 */
	private static final String goods = "GOODS";

	@Operation(summary = "查询会员收藏列表", description = "查询会员收藏列表")
	@RequestLogger
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping("/{type}")
	public Result<PageResult<StoreCollectionVO>> goodsListPage(
		@Parameter(description = "类型", required = true) @PathVariable String type,
		@Validated PageParam page) {
		if (goods.equals(type)) {
			IPage<GoodsCollectionVO> goodsCollectionPage = IMemberGoodsCollectionService.goodsCollection(page);
			return Result.success(PageResult.convertMybatisPage(goodsCollectionPage, StoreCollectionVO.class));
		}

		IPage<StoreCollectionVO> storeCollectionVOPage = storeCollectionService.storeCollection(page);
		return Result.success(PageResult.convertMybatisPage(storeCollectionVOPage, StoreCollectionVO.class));
	}

	@Operation(summary = "添加会员收藏", description = "添加会员收藏")
	@RequestLogger
	@PreAuthorize("@el.check('admin','timing:list')")
	@PostMapping("/{type}/{id}")
	public Result<Boolean> addGoodsCollection(
		@Parameter(description = "类型", required = true, example = "GOODS:商品,STORE:店铺") @PathVariable String type,
		@Parameter(description = "id", required = true) @NotNull(message = "值不能为空") @PathVariable Long id) {
		if (goods.equals(type)) {
			return Result.success(IMemberGoodsCollectionService.addGoodsCollection(id));
		}
		return Result.success(storeCollectionService.addStoreCollection(id));
	}

	@Operation(summary = "删除会员收藏", description = "删除会员收藏")
	@RequestLogger
	@PreAuthorize("@el.check('admin','timing:list')")
	@DeleteMapping(value = "/{type}/{id}")
	public Result<Object> deleteGoodsCollection(
		@Parameter(description = "类型", required = true, example = "GOODS:商品,STORE:店铺") @PathVariable String type,
		@Parameter(description = "id", required = true) @NotNull(message = "值不能为空") @PathVariable Long id) {
		if (goods.equals(type)) {
			return Result.success(IMemberGoodsCollectionService.deleteGoodsCollection(id));
		}
		return Result.success(storeCollectionService.deleteStoreCollection(id));
	}

	@Operation(summary = "查询会员是否收藏", description = "查询会员是否收藏")
	@RequestLogger("查询会员是否收藏")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/{type}/{id}/collection")
	public Result<Boolean> isCollection(
		@Parameter(description = "类型", required = true, example = "GOODS:商品,STORE:店铺") @PathVariable String type,
		@Parameter(description = "id", required = true) @NotNull(message = "值不能为空") @PathVariable Long id) {
		if (goods.equals(type)) {
			return Result.success(this.IMemberGoodsCollectionService.isCollection(id));
		}
		return Result.success(this.storeCollectionService.isCollection(id));
	}
}
