package com.taotao.cloud.goods.biz.controller.buyer;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.api.vo.CategoryVO;
import com.taotao.cloud.goods.biz.service.CategoryService;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 买家端,商品分类接口
 */
@Validated
@RestController
@Tag(name = "买家端-商品分类API", description = "买家端-商品分类API")
@RequestMapping("/buyer/goods/category")
public class CategoryBuyerController {

	/**
	 * 商品分类
	 */
	@Autowired
	private CategoryService categoryService;

	@Operation(summary = "获取商品分类列表", description = "获取商品分类列表", method = CommonConstant.GET)
	@RequestLogger(description = "获取商品分类列表")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{parentId}")
	public Result<List<CategoryVO>> list(
		@NotNull(message = "分类ID不能为空") @PathVariable String parentId) {
		return Result.success(categoryService.listAllChildren(parentId));
	}
}
