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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 店铺端-规格接口API
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "店铺端-规格API", description = "店铺端-规格API")
@RequestMapping("/goods/seller/goods/spec")
public class SpecificationStoreController {

	/**
	 * 商品规格服务
	 */
	private final CategorySpecificationService categorySpecificationService;

	@Operation(summary = "获取分类规格", description = "获取分类规格", method = CommonConstant.GET)
	@RequestLogger("获取分类规格")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{categoryId}")
	public Result<List<Specification>> getSpecifications(@PathVariable Long categoryId) {
		return Result.success(categorySpecificationService.getCategorySpecList(categoryId));
	}

}
