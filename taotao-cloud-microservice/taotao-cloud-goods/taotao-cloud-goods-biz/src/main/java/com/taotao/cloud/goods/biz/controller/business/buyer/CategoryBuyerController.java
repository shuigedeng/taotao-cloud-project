package com.taotao.cloud.goods.biz.controller.business.buyer;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.api.model.vo.CategoryTreeVO;
import com.taotao.cloud.goods.biz.service.business.ICategoryService;
import com.taotao.cloud.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * 买家端,商品分类接口
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-20 16:59:38
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "买家端-商品分类API", description = "买家端-商品分类API")
@RequestMapping("/goods/buyer/category")
public class CategoryBuyerController {

	/**
	 * 商品分类
	 */
	private final ICategoryService categoryService;

	@RequestLogger
	@Operation(summary = "根据父id获取商品分类列表", description = "根据父id获取商品分类列表")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{parentId}")
	public Result<List<CategoryTreeVO>> list(
		@Parameter(description = "父ID 0-最上级id") @NotNull(message = "父ID不能为空") @PathVariable Long parentId) {
		return Result.success(categoryService.listAllChildren(parentId));
	}
}
