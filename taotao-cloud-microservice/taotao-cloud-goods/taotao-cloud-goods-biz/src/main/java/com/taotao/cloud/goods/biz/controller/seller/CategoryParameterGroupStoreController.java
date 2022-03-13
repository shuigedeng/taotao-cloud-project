package com.taotao.cloud.goods.biz.controller.seller;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.api.vo.ParameterGroupVO;
import com.taotao.cloud.goods.biz.service.CategoryParameterGroupService;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺端,分类绑定参数组管理接口
 */
@Validated
@RestController
@Tag(name = "商户管理端-分类绑定参数组管理API", description = "商户管理端-分类绑定参数组管理API")
@RequestMapping("/goods/seller/store/goods/categoryParameters")
public class CategoryParameterGroupStoreController {

	@Autowired
	private CategoryParameterGroupService categoryParameterGroupService;

	@Operation(summary = "查询某分类下绑定的参数信息", description = "查询某分类下绑定的参数信息", method = CommonConstant.GET)
	@RequestLogger(description = "查询某分类下绑定的参数信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{category_id}")
	public Result<List<ParameterGroupVO>> getCategoryParam(@PathVariable("category_id") String categoryId) {
		return Result.success(categoryParameterGroupService.getCategoryParams(categoryId));
	}

}
