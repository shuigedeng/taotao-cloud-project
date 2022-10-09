package com.taotao.cloud.goods.biz.controller.business.seller;


import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.api.model.vo.SpecificationVO;
import com.taotao.cloud.goods.biz.model.convert.SpecificationConvert;
import com.taotao.cloud.goods.biz.model.entity.Specification;
import com.taotao.cloud.goods.biz.service.business.ICategorySpecificationService;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 店铺端-规格接口接口
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-14 20:51:29
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "店铺端-规格API", description = "店铺端-规格API")
@RequestMapping("/goods/seller/goods/specification")
public class SpecificationStoreController {

	/**
	 * 商品规格服务
	 */
	private final ICategorySpecificationService categorySpecificationService;

	@Operation(summary = "获取分类规格", description = "获取分类规格")
	@RequestLogger("获取分类规格")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{categoryId}")
	public Result<List<SpecificationVO>> getSpecifications(@PathVariable Long categoryId) {
		List<Specification> categorySpecList = categorySpecificationService.getCategorySpecList(
			categoryId);
		return Result.success(
			SpecificationConvert.INSTANCE.convert(categorySpecList));
	}

}
