package com.taotao.cloud.goods.biz.controller.seller;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.common.SecurityUtil;
import com.taotao.cloud.goods.api.vo.StoreGoodsLabelVO;
import com.taotao.cloud.goods.biz.entity.StoreGoodsLabel;
import com.taotao.cloud.goods.biz.service.StoreGoodsLabelService;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Objects;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 店铺端,店铺分类接口
 */
@Validated
@RestController
@Tag(name = "商户管理端-店铺分类API", description = "商户管理端-店铺分类API")
@RequestMapping("/goods/seller/store/goods/label")
public class GoodsLabelStoreController {

	/**
	 * 店铺分类
	 */
	@Autowired
	private StoreGoodsLabelService storeGoodsLabelService;

	@Operation(summary = "获取当前店铺商品分类列表", description = "获取当前店铺商品分类列表", method = CommonConstant.GET)
	@RequestLogger(description = "获取当前店铺商品分类列表")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping
	public Result<List<StoreGoodsLabelVO>> list() {
		String storeId = Objects.requireNonNull(SecurityUtil.getUser()).getStoreId();
		return Result.success(storeGoodsLabelService.listByStoreId(storeId));
	}

	@Operation(summary = "获取店铺商品分类详情", description = "获取店铺商品分类详情", method = CommonConstant.GET)
	@RequestLogger(description = "获取店铺商品分类详情")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/{id}")
	public Result<StoreGoodsLabel> getStoreGoodsLabel(@PathVariable String id) {
		return Result.success(storeGoodsLabelService.getById(id));
	}

	@Operation(summary = "添加店铺商品分类", description = "添加店铺商品分类", method = CommonConstant.POST)
	@RequestLogger(description = "添加店铺商品分类")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping
	public Result<StoreGoodsLabel> add(@Validated StoreGoodsLabel storeGoodsLabel) {
		//String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
		//storeGoodsLabel.setStoreId(storeId);
		return Result.success(storeGoodsLabelService.addStoreGoodsLabel(storeGoodsLabel));
	}

	@Operation(summary = "修改店铺商品分类", description = "修改店铺商品分类", method = CommonConstant.PUT)
	@RequestLogger(description = "修改店铺商品分类")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping
	public Result<Boolean> edit(@Validated StoreGoodsLabel storeGoodsLabel) {
		storeGoodsLabelService.getById(storeGoodsLabel.getId());
		return Result.success(storeGoodsLabelService.editStoreGoodsLabel(storeGoodsLabel));
	}

	@Operation(summary = "删除店铺商品分类", description = "删除店铺商品分类", method = CommonConstant.DELETE)
	@RequestLogger(description = "删除店铺商品分类")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@DeleteMapping("/{id}")
	public Result<Boolean> delete(@PathVariable String id) {
		storeGoodsLabelService.getById(id);
		storeGoodsLabelService.removeStoreGoodsLabel(id);
		return Result.success(true);
	}
}
