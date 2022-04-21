package com.taotao.cloud.goods.biz.controller.seller;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.api.vo.ParameterGroupVO;
import com.taotao.cloud.goods.biz.service.CategoryParameterGroupService;
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
 * 店铺端,分类绑定参数组接口
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-20 16:59:38
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "店铺端-分类绑定参数组API", description = "店铺端-分类绑定参数组API")
@RequestMapping("/goods/seller/category/parameters")
public class CategoryParameterGroupStoreController {

	/**
	 * 分类参数组服务
	 */
	private final CategoryParameterGroupService categoryParameterGroupService;

	@Operation(summary = "查询某分类下绑定的参数信息", description = "查询某分类下绑定的参数信息")
	@RequestLogger("查询某分类下绑定的参数信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{categoryId}")
	public Result<List<ParameterGroupVO>> getCategoryParam(@PathVariable("categoryId") Long categoryId) {
		return Result.success(categoryParameterGroupService.getCategoryParams(categoryId));
	}

}
