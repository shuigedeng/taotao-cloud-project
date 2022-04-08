package com.taotao.cloud.goods.biz.controller.seller;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.biz.entity.Specification;
import com.taotao.cloud.goods.biz.service.CategorySpecificationService;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺端,商品分类规格接口
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "商户管理端-商品分类规格API", description = "商户管理端-商品分类规格API")
@RequestMapping("/goods/seller/store/goods/categorySpec")
public class CategorySpecificationStoreController {

	/**
	 * 商品规格服务
	 */
	private final CategorySpecificationService categorySpecificationService;

	@Operation(summary = "查询某分类下绑定的规格信息", description = "查询某分类下绑定的规格信息", method = CommonConstant.GET)
	@RequestLogger("查询某分类下绑定的规格信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{categoryId}")
	public Result<List<Specification>> getCategorySpec(@PathVariable("categoryId") Long categoryId) {
		return Result.success(categorySpecificationService.getCategorySpecList(categoryId));
	}


}
