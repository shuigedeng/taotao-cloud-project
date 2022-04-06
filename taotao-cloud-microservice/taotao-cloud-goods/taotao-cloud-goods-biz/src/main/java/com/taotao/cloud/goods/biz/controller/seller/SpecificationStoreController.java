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
 * 商户管理端-规格接口API
 */
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/goods/seller/store/goods/spec")
@Tag(name = "商户管理端-规格接口API", description = "商户管理端-规格接口API")
public class SpecificationStoreController {

	/**
	 * 商品规格服务
	 */
	private final CategorySpecificationService categorySpecificationService;

	@Operation(summary = "获取分类规格", description = "获取分类规格", method = CommonConstant.GET)
	@RequestLogger(description = "获取分类规格")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{categoryId}")
	public Result<List<Specification>> getSpecifications(@PathVariable String categoryId) {
		return Result.success(categorySpecificationService.getCategorySpecList(categoryId));
	}

}
