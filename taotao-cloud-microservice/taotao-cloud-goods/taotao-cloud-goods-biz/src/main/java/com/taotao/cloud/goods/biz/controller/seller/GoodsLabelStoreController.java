package com.taotao.cloud.goods.biz.controller.seller;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.common.SecurityUtil;
import com.taotao.cloud.goods.api.dto.StoreGoodsLabelDTO;
import com.taotao.cloud.goods.api.vo.StoreGoodsLabelInfoVO;
import com.taotao.cloud.goods.api.vo.StoreGoodsLabelVO;
import com.taotao.cloud.goods.biz.entity.StoreGoodsLabel;
import com.taotao.cloud.goods.biz.mapstruct.IGoodsLabelStoreMapStruct;
import com.taotao.cloud.goods.biz.service.IStoreGoodsLabelService;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 店铺端,店铺分类接口
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-14 21:49:55
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "店铺端-店铺分类API", description = "店铺端-店铺分类API")
@RequestMapping("/goods/seller/label")
public class GoodsLabelStoreController {

	/**
	 * 店铺分类服务
	 */
	private final IStoreGoodsLabelService storeGoodsLabelService;

	@Operation(summary = "获取当前店铺商品分类列表", description = "获取当前店铺商品分类列表")
	@RequestLogger("获取当前店铺商品分类列表")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping
	public Result<List<StoreGoodsLabelVO>> list() {
		Long storeId = SecurityUtil.getUser().getStoreId();
		return Result.success(storeGoodsLabelService.listByStoreId(storeId));
	}

	@Operation(summary = "获取店铺商品分类详情", description = "获取店铺商品分类详情")
	@RequestLogger("获取店铺商品分类详情")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/{id}")
	public Result<StoreGoodsLabelInfoVO> getStoreGoodsLabel(@PathVariable Long id) {
		StoreGoodsLabel storeGoodsLabel = storeGoodsLabelService.getById(id);
		return Result.success(
			IGoodsLabelStoreMapStruct.INSTANCE.storeGoodsLabelToStoreGoodsLabelInfoVO(
				storeGoodsLabel
			));
	}

	@Operation(summary = "添加店铺商品分类", description = "添加店铺商品分类")
	@RequestLogger("添加店铺商品分类")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping
	public Result<Boolean> add(@Validated @RequestBody StoreGoodsLabelDTO storeGoodsLabelDTO) {
		StoreGoodsLabel storeGoodsLabel = IGoodsLabelStoreMapStruct.INSTANCE.storeGoodsLabelDTOToStoreGoodsLabel(
			storeGoodsLabelDTO);
		return Result.success(storeGoodsLabelService.addStoreGoodsLabel(storeGoodsLabel));
	}

	@Operation(summary = "修改店铺商品分类", description = "修改店铺商品分类")
	@RequestLogger("修改店铺商品分类")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping("/{id}")
	public Result<Boolean> edit(@PathVariable Long id, @Validated @RequestBody StoreGoodsLabelDTO storeGoodsLabelDTO) {
		StoreGoodsLabel storeGoodsLabel = IGoodsLabelStoreMapStruct.INSTANCE.storeGoodsLabelDTOToStoreGoodsLabel(
			storeGoodsLabelDTO);
		storeGoodsLabel.setId(id);
		return Result.success(storeGoodsLabelService.editStoreGoodsLabel(storeGoodsLabel));
	}

	@Operation(summary = "删除店铺商品分类", description = "删除店铺商品分类")
	@RequestLogger("删除店铺商品分类")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@DeleteMapping("/{id}")
	public Result<Boolean> delete(@PathVariable Long id) {
		return Result.success(storeGoodsLabelService.removeStoreGoodsLabel(id));
	}
}
